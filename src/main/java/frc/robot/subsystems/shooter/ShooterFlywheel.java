package frc.robot.subsystems.shooter;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.Gains;
import frc.robot.util.MotorUtils;
import frc.robot.util.Toggleable;

/**
 * Controls and operators the Shooter Flywheel.
 * 
 * @author Akil Pathiranage
 */
public final class ShooterFlywheel extends SubsystemBase implements Toggleable {
    public static final double FLYWHEEL_ROTATION_RATIO = Constants.Falcon500.unitsPerRotation / 600.0;
    
    private final WPI_TalonFX     mot_upper;
    private final WPI_TalonFX     mot_lower;
    private double                upperTarget;
    private double                lowerTarget;
    private boolean               enabled;

    private ShuffleboardTab       tab;
    private HashMap<String, NetworkTableEntry> fields;

    /**
     * Constructs a flywheel subsystem.
     */
    public ShooterFlywheel() {
        mot_upper = new WPI_TalonFX(Constants.ShooterFlywheel.UPPER_MOTOR_ID);
            mot_upper.configFactoryDefault();
            mot_upper.setNeutralMode(NeutralMode.Coast);
        MotorUtils.setGains(mot_upper, 0, Constants.ShooterFlywheel.UPPER_GAINS);

        mot_lower = new WPI_TalonFX(Constants.ShooterFlywheel.LOWER_MOTOR_ID);
            mot_lower.configFactoryDefault();
            mot_lower.setNeutralMode(NeutralMode.Coast);
            mot_lower.setInverted(true);
        MotorUtils.setGains(mot_lower, 0, Constants.ShooterFlywheel.UPPER_GAINS);

    
        enabled = false;
        upperTarget = 0;
        lowerTarget = 0;

        fields = new HashMap<String, NetworkTableEntry>();

        tab = Shuffleboard.getTab("Flywheel");
        ShuffleboardLayout flywheelLayout = tab.getLayout("flywheelLayout", BuiltInLayouts.kList);
        fields.put("upperTarget",
                flywheelLayout.add("Upper target", upperTarget).withWidget(BuiltInWidgets.kNumberSlider)
                        .withProperties(Map.of("min", 0, "max", 6000, "blockincrement", 500)).getEntry());
        fields.put("lowerTarget",
                flywheelLayout.add("Lower target", lowerTarget).withWidget(BuiltInWidgets.kNumberSlider)
                        .withProperties(Map.of("min", 0, "max", 6000, "blockincrement", 500)).getEntry());
        fields.put("rpmUpper", flywheelLayout.add("Current Upper RPM", getUpperVelocity()).getEntry());
        fields.put("rpmLower", flywheelLayout.add("Current Lower RPM", getLowerVelocity()).getEntry());
        fields.put("subsystemEnabled", flywheelLayout.add("Subsystem Enabled: ", enabled).getEntry());

        ShuffleboardLayout pidTuningLayout = tab.getLayout("PID Tuning Controls", BuiltInLayouts.kList);
        fields.put("lp",     pidTuningLayout.add("Lower P Const: ", Constants.ShooterFlywheel.LOWER_GAINS.P).getEntry());
        fields.put("li",     pidTuningLayout.add("Lower I const: ", Constants.ShooterFlywheel.LOWER_GAINS.I).getEntry());
        fields.put("ld",     pidTuningLayout.add("Lower D Const: ", Constants.ShooterFlywheel.LOWER_GAINS.D).getEntry());
        fields.put("lf",     pidTuningLayout.add("Lower F const: ", Constants.ShooterFlywheel.LOWER_GAINS.F).getEntry());
        fields.put("up",     pidTuningLayout.add("Upper P Const: ", Constants.ShooterFlywheel.UPPER_GAINS.P).getEntry());
        fields.put("ui",     pidTuningLayout.add("Upper I const: ", Constants.ShooterFlywheel.UPPER_GAINS.I).getEntry());
        fields.put("ud",     pidTuningLayout.add("Upper D Const: ", Constants.ShooterFlywheel.UPPER_GAINS.D).getEntry());
        fields.put("uf",     pidTuningLayout.add("Upper F const: ", Constants.ShooterFlywheel.UPPER_GAINS.F).getEntry());
        fields.put("change", pidTuningLayout.add("Change values", false).withWidget(BuiltInWidgets.kToggleButton).getEntry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {
        fields.get("subsystemEnabled").setBoolean(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void simulationPeriodic() {
        fields.get("rpmLower").setDouble(getLowerVelocity());
        fields.get("rpmUpper").setDouble(getUpperVelocity());
        fields.get("subsystemEnabled").setBoolean(enabled);
    }

    /**
     * Method for enabing the flywheel.
     */
    public void enable() {
        enabled = true;
    }

    /**
     * Method for disabling the flywheel.
     */
    public void disable() {
        enabled = false;

        mot_lower.stopMotor();
        mot_upper.stopMotor();
    }

    public void setVelocityTarget(double target) {
        if (!enabled) return;
        setLowerTarget(target);
        setUpperTarget(target);
    }

    /**
     * Method that sets the target for the shooter flywheel.
     * 
     * @param target Target revolutions per minute.
     */
    public void setLowerTarget(double target) {
        if (!enabled) return;

        // 600 since its rotation speed is in position change/100ms
        mot_lower.set(ControlMode.Velocity, lowerTarget * FLYWHEEL_ROTATION_RATIO);
        lowerTarget = target;
    }

    /**
     * Method that sets the target for the shooter flywheel.
     * 
     * @param target Target revolutions per minute.
     */
    public void setUpperTarget(double target) {
        if (!enabled) return;
        // 600 since its rotation speed is in position change/100ms
        mot_upper.set(ControlMode.Velocity, upperTarget * FLYWHEEL_ROTATION_RATIO);
        upperTarget = target;
    }

    /**
     * Method for configuring the PID of the motor.
     * 
     * @param motor Motor to configure.
     * @param p  P constant
     * @param i  I constant
     * @param d  D constant
     * @param ff Feedforward constant
     */
    protected void setGains(WPI_TalonFX motor, Gains gains) {
        motor.config_kP(0, gains.P);
        motor.config_kI(0, gains.I);
        motor.config_kD(0, gains.D);
        motor.config_kF(0, gains.F);
    }
    /**
     * Method for getting the RPM of the main motor.
     * 
     * @return A double representing the RPM of the motor.
     */
    public double getVelocity() {
        return (getLowerVelocity() + getUpperVelocity()) * 0.5;
    }

    /**
     * Method for getting the RPM of the main motor.
     * 
     * @return A double representing the RPM of the motor.
     */
    public double getLowerVelocity() {
        return mot_lower.getSelectedSensorVelocity() * Constants.Falcon500.unitsPerRotation;
    }

    /**
     * Method for getting the RPM of the main motor.
     * 
     * @return A double representing the RPM of the motor.
     */
    public double getUpperVelocity() {
        return mot_upper.getSelectedSensorVelocity() * Constants.Falcon500.unitsPerRotation;
    }

    public boolean isUpperTargetReached() {
        return Math.abs(upperTarget - getUpperVelocity()) <= Constants.ShooterFlywheel.SHOOTER_TOLERANCE;
    }

    public boolean isLowerTargetReached() {
        return Math.abs(lowerTarget - getLowerVelocity()) <= Constants.ShooterFlywheel.SHOOTER_TOLERANCE;
    }

    public boolean isTargetReached() {
        return isUpperTargetReached() && isLowerTargetReached();
    }

    /**
     * Method for getting if the subsystem is enabled.
     * 
     * @return True if the subsystem is enabled, false if not.
     */
    public boolean isEnabled() {
        return enabled;
    }

}