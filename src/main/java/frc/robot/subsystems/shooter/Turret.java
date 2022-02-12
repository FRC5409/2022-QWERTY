package frc.robot.subsystems.shooter;

import java.util.HashMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase {

    // private WPI_TalonFX mot_main;
    private CANSparkMax mot_main;
    private RelativeEncoder enc_main;
    private SparkMaxPIDController controller_main;
    // private SparkMaxLimitSwitch limit_switch;
    private DigitalInput limit_switch;

    private boolean enabled;

    private HashMap<String, NetworkTableEntry> shuffleboardFields;
    private double target;

    /**
     * Constructor for the turret.
     */
    public Turret() {
        mot_main = new CANSparkMax(Constants.Turret.MAIN_MOTOR_ID, MotorType.kBrushless);
        mot_main.restoreFactoryDefaults();
        mot_main.setIdleMode(IdleMode.kCoast);

        enc_main = mot_main.getEncoder();
        enc_main.setPosition(0);

        controller_main = mot_main.getPIDController();
        controller_main.setOutputRange(-0.1, 0.1);
        configPID(Constants.Turret.P, Constants.Turret.I, Constants.Turret.D, Constants.Turret.F);
        enabled = false;

        limit_switch = new DigitalInput(Constants.Turret.LIMIT_SWITCH_CHANNEL);

        shuffleboardFields = new HashMap<String, NetworkTableEntry>();

        ShuffleboardTab tab = Shuffleboard.getTab("Turret");
        ShuffleboardLayout shooterControls = tab.getLayout("Turret Controls: ", BuiltInLayouts.kList);
        shuffleboardFields.put("target", shooterControls.add("Target", 0.0).getEntry());
        // shooterControls.add("Toggle Subsystem", new EnableTurret(this));
        shuffleboardFields.put("encoderVals", shooterControls.add("Encoder Values", 0.0).getEntry());
        shuffleboardFields.put("angle", shooterControls.add("Angle", 0).getEntry());
        shuffleboardFields.put("enabled", shooterControls.add("Enabled", false).getEntry());

        ShuffleboardLayout PIDTuning = tab.getLayout("PID Tuning:", BuiltInLayouts.kList);
        shuffleboardFields.put("p", PIDTuning.add("P", Constants.Turret.P).getEntry());
        shuffleboardFields.put("i", PIDTuning.add("I", Constants.Turret.I).getEntry());
        shuffleboardFields.put("d", PIDTuning.add("D", Constants.Turret.D).getEntry());
        shuffleboardFields.put("f", PIDTuning.add("F", Constants.Turret.F).getEntry());
        shuffleboardFields.put("change",
                PIDTuning.add("Change", false).withWidget(BuiltInWidgets.kToggleButton).getEntry());
        shuffleboardFields.put("enabled", shooterControls.add("Enabled", false).getEntry());


        configPID(Constants.Turret.P, Constants.Turret.I, Constants.Turret.D, Constants.Turret.F);

        // tab.add("PID", controller_main);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {
        if (!limit_switch.get()) {
            enc_main.setPosition(0);
        }
        target = shuffleboardFields.get("target").getDouble(0);
        shuffleboardFields.get("encoderVals").setDouble(enc_main.getPosition());
        shuffleboardFields.get("angle").setDouble(getRotationAngle());
        shuffleboardFields.get("enabled").setBoolean(enabled);
        if (shuffleboardFields.get("change").getBoolean(false)) {
            disable();
            configPID(shuffleboardFields.get("p").getDouble(0), shuffleboardFields.get("i").getDouble(0),
                    shuffleboardFields.get("d").getDouble(0),
                    shuffleboardFields.get("f").getDouble(0));
            shuffleboardFields.get("change").setBoolean(false);
        }
    }

    /**
     * Method for enabling the turret subsystem.
     */
    public void enable() {
        enabled = true;
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
     * @param newTarget Angle to turn to in degrees, can be negative for turning
     *                  left, positive for turning right.
     */
    public void setRotationTarget(double newTarget) {
        target = newTarget;
    }

    /**
     * Method for setting the rotation target of the turret, uses the value
     * previously stored by the program.
     */
    public void turnToTarget() {
        if (!enabled) {
            return;
        }

        double newPosition = target + enc_main.getPosition();

        if (enabled) {
            if (newPosition > Constants.Turret.UPPER_LIMIT) {
                newPosition = Constants.Turret.UPPER_LIMIT;
            }
            if (newPosition < Constants.Turret.LOWER_LIMIT) {
                newPosition = Constants.Turret.LOWER_LIMIT;
            }
            controller_main.setReference(newPosition, CANSparkMax.ControlType.kPosition);
        }
        // mot_main.set(TalonFXControlMode.Position, positionForMotor);

    }

    public void turnToAngle(double angle) {
        if (!enabled) {
            return;
        }

        double motorRotationPerDegree = Constants.Turret.GEAR_RATIO / 360;
        double newPosition = motorRotationPerDegree * angle + enc_main.getPosition();
        if (enabled) {
            if (newPosition > Constants.Turret.UPPER_LIMIT) {
                newPosition = Constants.Turret.UPPER_LIMIT;
            }
            if (newPosition < Constants.Turret.LOWER_LIMIT) {
                newPosition = Constants.Turret.LOWER_LIMIT;
            }
            controller_main.setReference(newPosition, CANSparkMax.ControlType.kPosition);
        }

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
    public double getRotationAngle() {
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
