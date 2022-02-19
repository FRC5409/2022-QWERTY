package frc.robot.subsystems.shooter;

import java.util.HashMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.Gains;
import frc.robot.util.Toggleable;

/**
 * 
 * @author Akil Pathiranage, Keith Davies
 */
public class ShooterTurret extends SubsystemBase implements Toggleable {
    public static double MOTOR_ROTATION_RATIO = (Constants.Turret.GEAR_RATIO / 360);
    public static double MOTOR_TARGET_RATIO = 1 / MOTOR_ROTATION_RATIO;

    // private WPI_TalonFX mot_main;
    private CANSparkMax                        mot_main;
    private RelativeEncoder                    enc_main;
    private SparkMaxPIDController              controller_main;

    private boolean                            enabled;

    private HashMap<String, NetworkTableEntry> fields;
    private double                             target;

    private DoubleSolenoid                     dsl_hood; 

    private DigitalInput                       limit_switch;
    

    /**
     * Constructor for the turret.
     */
    public ShooterTurret() {
        mot_main = new CANSparkMax(Constants.Turret.MAIN_MOTOR_ID, MotorType.kBrushless);
            mot_main.restoreFactoryDefaults();
            mot_main.setIdleMode(IdleMode.kCoast);

        enc_main = mot_main.getEncoder();
            enc_main.setPosition(0);

        controller_main = mot_main.getPIDController();
            controller_main.setOutputRange(-0.1, 0.1);

        limit_switch = new DigitalInput(Constants.Turret.LIMIT_SWITCH_CHANNEL);

        dsl_hood = new DoubleSolenoid(Constants.Turret.HOOD_MODULE, PneumaticsModuleType.REVPH, 
            Constants.Turret.HOOD_FORWARD_CHANNEL, Constants.Turret.HOOD_REVERSE_CHANNEL);

        enabled = false;
        fields = new HashMap<String, NetworkTableEntry>();




        ShuffleboardTab tab = Shuffleboard.getTab("Turret");

        ShuffleboardLayout shooterControls = tab.getLayout("Turret Controls: ", BuiltInLayouts.kList);
            fields.put("target",      shooterControls.add("Target", 0.0).getEntry());
            fields.put("encoderVals", shooterControls.add("Encoder Values", 0.0).getEntry());
            fields.put("limitswitch", shooterControls.add("Limit Switch Reading", true).getEntry());
            fields.put("hood", shooterControls.add("Hood position", "Up").getEntry());

        ShuffleboardLayout PIDTuning = tab.getLayout("PID Tuning:", BuiltInLayouts.kList);
            fields.put("p",           PIDTuning.add("P", controller_main.getP()).getEntry());
            fields.put("i",           PIDTuning.add("I", controller_main.getI()).getEntry());
            fields.put("d",           PIDTuning.add("D", controller_main.getD()).getEntry());
            fields.put("f",           PIDTuning.add("F", controller_main.getFF()).getEntry());
            fields.put("enabled",     shooterControls.add("Enabled", false).getEntry());
            fields.put("change",      PIDTuning.add("Change", false)
                                      .withWidget(BuiltInWidgets.kToggleButton).getEntry());

        setGains(Constants.Shooter.TURRET_GAINS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {
        target = fields.get("target").getDouble(0);
        fields.get("encoderVals").setDouble(enc_main.getPosition());
        fields.get("enabled").setBoolean(enabled);
        fields.get("limitswitch").setBoolean(limit_switch.get());

        if(dsl_hood.get().equals(Value.kForward)){
            fields.get("hood").setString("Up");
        } else if (dsl_hood.get().equals(Value.kReverse)){
            fields.get("hood").setString("Down");
        } else if (dsl_hood.get().equals(Value.kOff)){
            fields.get("hood").setString("Off");
        }



        setGains(
            new Gains(    
                fields.get("p").getDouble(0), 
                fields.get("i").getDouble(0),
                fields.get("d").getDouble(0),
                fields.get("f").getDouble(0)
            )
        );
    }

    /**
     * Method for enabling the turret subsystem.
     */
    public void enable() {
        enabled = true;
    }

    public void reset() {
        setRotationTarget(0);
    }

    /**
     * Method for disabling the turret subsystem.
     */
    public void disable() {
        enabled = false;
        mot_main.disable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Method for setting the rotation target of the turret.
     * 
     * @param value Angle to turn to in degrees, can be negative for turning
     *                  left, positive for turning right.
     */
    public void setRotationTarget(double value) {
        if (!enabled) return;

        // TODO add safetys
        controller_main.setReference(
            Constants.Turret.LIMITS.clamp(MOTOR_ROTATION_RATIO * value), 
            CANSparkMax.ControlType.kPosition);
        
        target = value;
    }

    /**
     * Method for getting the current rotation target.
     * 
     * @return Angle in degrees.
     */
    public double getRotationTarget() {
        return target;
    }
    
    /**
     * Method for getting the current rotation.
     * 
     * @return Angle in degrees.
     */
    public double getRotation() {
        return enc_main.getPosition() * MOTOR_TARGET_RATIO;
    }

    /**
     * Method for getting if the turret is aligned with its target.
     * 
     * @return True if its aligned, false if not.
     */
    public boolean isTargetReached() {
        return Math.abs(getRotation() - target) < Constants.Turret.ALIGNMENT_THRESHOLD;
    }

    public void setGains(Gains gains) {
        controller_main.setP(gains.P);
        controller_main.setI(gains.I);
        controller_main.setD(gains.D);
        controller_main.setFF(gains.F);
    }

    
    /**
     * Method for setting the hood to the up position.
     */
    public void hoodUpPosition(){
        if(!enabled) return;
        dsl_hood.set(Value.kForward);
    }

    /**
     * Method for setting the hood to the down position.
     */
    public void hoodDownPosition(){
        if(!enabled) return;
        dsl_hood.set(Value.kReverse);
    }

}
