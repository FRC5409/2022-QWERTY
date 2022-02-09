package frc.robot.subsystems.shooter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.trajectory.TrajectoryGenerator.ControlVectorList;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.EnableTurret;

public class Turret extends SubsystemBase {

    // private WPI_TalonFX mot_main;
    private CANSparkMax mot_main;
    private RelativeEncoder enc_main;
    private SparkMaxPIDController controller_main;

    private boolean enabled;

    private HashMap<String, NetworkTableEntry> shuffleboardFields;
    private double target;

    /**
     * Constructor for the turret.
     */
    public Turret() {
        mot_main = new CANSparkMax(Constants.kTurret.MAIN_MOTOR_ID, MotorType.kBrushless);
        mot_main.restoreFactoryDefaults();
        mot_main.setIdleMode(IdleMode.kCoast);

        enc_main = mot_main.getEncoder();
        enc_main.setPosition(0);

        controller_main = mot_main.getPIDController();
        enabled = false;

        shuffleboardFields = new HashMap<String, NetworkTableEntry>();

        ShuffleboardTab tab = Shuffleboard.getTab("Turret");
        ShuffleboardLayout shooterControls = tab.getLayout("Turret Controls: ", BuiltInLayouts.kList);
        shuffleboardFields.put("target", shooterControls.add("Target", 0.0).withWidget(BuiltInWidgets.kNumberSlider)
                .withProperties(Map.of("min", -20, "max", 20)).getEntry());
        shooterControls.add("Toggle Subsystem", new EnableTurret(this));
        shuffleboardFields.put("encoderVals", shooterControls.add("Encoder Values", 0.0).getEntry());

        ShuffleboardLayout PIDTuning = tab.getLayout("PID Tuning:", BuiltInLayouts.kList);
        shuffleboardFields.put("p", PIDTuning.add("P", 0).getEntry());
        shuffleboardFields.put("i", PIDTuning.add("I", 0).getEntry());
        shuffleboardFields.put("d", PIDTuning.add("D", 0).getEntry());
        shuffleboardFields.put("f", PIDTuning.add("F", 0).getEntry());
        shuffleboardFields.put("change",
                PIDTuning.add("Change", false).withWidget(BuiltInWidgets.kToggleButton).getEntry());

        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {

        target = shuffleboardFields.get("target").getDouble(0);
        shuffleboardFields.get("encoderVals").setDouble(enc_main.getPosition());

        if (shuffleboardFields.get("change").getBoolean(false)) {
            disable();
            configPID(shuffleboardFields.get("p").getDouble(0), shuffleboardFields.get("i").getDouble(0),
                    shuffleboardFields.get("d").getDouble(0),
                    shuffleboardFields.get("f").getDouble(0));
        }
    }

    /**
     * Method for enabling the turret subsystem.
     */
    public void enable() {
        enabled = true;
        setRotationTarget();
    }

    /**
     * Method for disabling the turret subsystem.
     */
    public void disable() {
        enabled = false;
        mot_main.disable();
    }

    public boolean isEnabled(){
        return enabled;
    }

    /**
     * Method for setting the rotation target of the turret.
     * 
     * @param newTarget Angle to turn to in degrees, can be negative for turning
     *                  left, positive for turning right.
     */
    public void setRotationTarget(double newTarget) {
        // TODO add safetys

        target = newTarget;

        double motorRotationPerDegree = Constants.Turret.GEAR_RATIO / 360;

        controller_main.setReference(motorRotationPerDegree * target, CANSparkMax.ControlType.kPosition);
        // mot_main.set(TalonFXControlMode.Position, positionForMotor);

    }

    /**
     * Method for setting the rotation target of the turret, uses the value previously stored by the program. 
     */
    public void setRotationTarget() {
        // TODO add safetys

        double motorRotationPerDegree = Constants.Turret.GEAR_RATIO / 360;

        controller_main.setReference(motorRotationPerDegree * target, CANSparkMax.ControlType.kPosition);
        // mot_main.set(TalonFXControlMode.Position, positionForMotor);

    }

    /**
     * Method for getting the rotation target of the turret.
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
        double motorRotationPerDegree = Constants.Turret.GEAR_RATIO / 360;
        return enc_main.getPosition() / motorRotationPerDegree;
    }

    /**
     * Method for getting if the turret is aligned with its target.
     * 
     * @return True if its aligned, false if not.
     */
    public boolean isAligned() {

        return false;
    }

    public void configPID(double P, double I, double D, double F) {
        controller_main.setP(P);
        controller_main.setI(I);
        controller_main.setD(D);
        controller_main.setFF(F);
    }

}
