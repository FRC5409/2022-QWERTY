package frc.robot.subsystems.shooter;

import java.util.HashMap;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.LayoutType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Controls and operators the Shooter Flywheel.
 * 
 * @author Akil Pathiranage
 */
public final class ShooterFlywheel extends SubsystemBase {

    WPI_TalonFX mot_upper;
    WPI_TalonFX mot_lower;

    protected final CANSparkMax indexerShooter_neo;
    protected RelativeEncoder enc_shooter;

    // pid controller
    private SparkMaxPIDController pidController;

    double speedShoot = 0;

    double upperTarget;
    double lowerTarget;
    boolean enabled;

    ShuffleboardTab tab;
    HashMap<String, NetworkTableEntry> shuffleBoardFields;

    private boolean preshooterEnabled;

    /**
     * Constructs a flywheel subsystem.
     */
    public ShooterFlywheel() {
        preshooterEnabled = false;

        mot_upper = new WPI_TalonFX(Constants.ShooterFlywheel.UPPER_MOTOR_ID);
        mot_lower = new WPI_TalonFX(Constants.ShooterFlywheel.LOWER_MOTOR_ID);

        mot_upper.configFactoryDefault();
        mot_lower.configFactoryDefault();

        mot_upper.setNeutralMode(NeutralMode.Coast);
        // mot_upper.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true,
        // 39, 40, 1));
        configPID(mot_upper, Constants.ShooterFlywheel.UPPER_P, Constants.ShooterFlywheel.UPPER_I,
               Constants.ShooterFlywheel.UPPER_D, Constants.ShooterFlywheel.UPPER_FF);

        mot_lower.setNeutralMode(NeutralMode.Coast);
        // mot_lower.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true,
        // 39, 40, 1));
        mot_lower.setInverted(true);
        configPID(mot_lower, Constants.ShooterFlywheel.LOWER_P, Constants.ShooterFlywheel.LOWER_I,
                Constants.ShooterFlywheel.LOWER_D, Constants.ShooterFlywheel.LOWER_FF);

        // test motor for indexer to shooter (flywheel thing)
        indexerShooter_neo = new CANSparkMax(Constants.kIndexer.kIndexShooterMotor, MotorType.kBrushless);
        indexerShooter_neo.setSmartCurrentLimit(20);
        indexerShooter_neo.setIdleMode(IdleMode.kBrake);
        indexerShooter_neo.setInverted(true);
        indexerShooter_neo.burnFlash();

        enc_shooter = indexerShooter_neo.getEncoder();

        // initialize PID controller
        pidController = indexerShooter_neo.getPIDController();

        configPID(pidController, Constants.kIndexer.UPPER_P, Constants.kIndexer.UPPER_I, Constants.kIndexer.UPPER_D,
                Constants.kIndexer.UPPER_F);

        enabled = false;
        upperTarget = 0;
        lowerTarget = 0;

        // shuffleboard data initialization
        // this data is updated in periodic of this subsystem

        shuffleBoardFields = new HashMap<String, NetworkTableEntry>();

        tab = Shuffleboard.getTab("Flywheel");
        ShuffleboardLayout flywheelLayout = tab.getLayout("flywheelLayout", BuiltInLayouts.kList);
        shuffleBoardFields.put("upperTarget",
                flywheelLayout.add("Upper target", upperTarget).withWidget(BuiltInWidgets.kNumberSlider)
                        .withProperties(Map.of("min", 0, "max", 6000, "blockincrement", 500)).getEntry());
        shuffleBoardFields.put("lowerTarget",
                flywheelLayout.add("Lower target", lowerTarget).withWidget(BuiltInWidgets.kNumberSlider)
                        .withProperties(Map.of("min", 0, "max", 6000, "blockincrement", 500)).getEntry());
        shuffleBoardFields.put("rpmUpper", flywheelLayout.add("Current Upper RPM", getUpperRPM()).getEntry());
        shuffleBoardFields.put("rpmLower", flywheelLayout.add("Current Lower RPM", getLowerRPM()).getEntry());
        shuffleBoardFields.put("subsystemEnabled", flywheelLayout.add("Subsystem Enabled: ", enabled).getEntry());

        ShuffleboardLayout pidTuningLayout = tab.getLayout("PID Tuning Controls", BuiltInLayouts.kList);
        shuffleBoardFields.put("lp",
                pidTuningLayout.add("Lower P Const: ", Constants.ShooterFlywheel.LOWER_P).getEntry());
        shuffleBoardFields.put("li",
                pidTuningLayout.add("Lower I const: ", Constants.ShooterFlywheel.LOWER_I).getEntry());
        shuffleBoardFields.put("ld",
                pidTuningLayout.add("Lower D Const: ", Constants.ShooterFlywheel.LOWER_D).getEntry());
        shuffleBoardFields.put("lf",
                pidTuningLayout.add("Lower F const: ", Constants.ShooterFlywheel.LOWER_FF).getEntry());
        shuffleBoardFields.put("up",
                pidTuningLayout.add("Upper P Const: ", Constants.ShooterFlywheel.UPPER_P).getEntry());
        shuffleBoardFields.put("ui",
                pidTuningLayout.add("Upper I const: ", Constants.ShooterFlywheel.UPPER_I).getEntry());
        shuffleBoardFields.put("ud",
                pidTuningLayout.add("Upper D Const: ", Constants.ShooterFlywheel.UPPER_D).getEntry());
        shuffleBoardFields.put("uf",
                pidTuningLayout.add("Upper F const: ", Constants.ShooterFlywheel.UPPER_FF).getEntry());
        shuffleBoardFields.put("change",
                pidTuningLayout.add("Change values", false).withWidget(BuiltInWidgets.kToggleButton).getEntry());

        /*
         * ShuffleboardLayout mLayout = tab.getLayout("motor layout",
         * BuiltInLayouts.kList);
         * shuffleBoardFields.put("preshooterTarget",
         * mLayout.add("preshooterTargetSpeed",
         * speedShoot).withWidget(BuiltInWidgets.kNumberSlider)
         * .withProperties(Map.of("min", 0, "max", 5000, "block increment",
         * 100)).getEntry());
         * shuffleBoardFields.put("preshooterSpeed",
         * mLayout.add("current speed of shoot", 0).getEntry());
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {

        // setUpperRPMTarget(shuffleBoardFields.get("upperTarget").getDouble(50));
        // setLowerRPMTarget(shuffleBoardFields.get("lowerTarget").getDouble(50));

        upperTarget = shuffleBoardFields.get("upperTarget").getDouble(50);
        lowerTarget = shuffleBoardFields.get("lowerTarget").getDouble(50);
        shuffleBoardFields.get("rpmLower").setDouble(getLowerRPM());
        shuffleBoardFields.get("rpmUpper").setDouble(getUpperRPM());
        shuffleBoardFields.get("subsystemEnabled").setBoolean(enabled);
        if (shuffleBoardFields.get("change").getBoolean(false)) {
            disable();
            configPID(mot_upper, shuffleBoardFields.get("up").getDouble(0),
                    shuffleBoardFields.get("ui").getDouble(0),
                    shuffleBoardFields.get("ud").getDouble(0),
                    shuffleBoardFields.get("uf").getDouble(0));

            configPID(mot_lower, shuffleBoardFields.get("lp").getDouble(0),
                    shuffleBoardFields.get("li").getDouble(0),
                    shuffleBoardFields.get("ld").getDouble(0),
                    shuffleBoardFields.get("lf").getDouble(0));
            System.out.println("Pid configured");
            shuffleBoardFields.get("change").setBoolean(false);
        }

        shuffleBoardFields.get("subsystemEnabled").setBoolean(enabled);

        /*if (shuffleBoardFields.get("change").getBoolean(false)) {
            indexShootOff();
            configPID(pidController, shuffleBoardFields.get("P").getDouble(0), shuffleBoardFields.get("I").getDouble(0),
                    shuffleBoardFields.get("D").getDouble(0),
                    shuffleBoardFields.get("F").getDouble(0));

            System.out.println("PID Configured");
            shuffleBoardFields.get("change").setBoolean(false);
        }*/

    }

    /**
     * Method for enabing the flywheel.
     */
    public void enable() {
        enabled = true;
        spinLower();
        spinUpper();
    }

    /**
     * Method for disabling the flywheel.
     */
    public void disable() {
        enabled = false;
        stopLower();
        stopUpper();
    }

    /**
     * Method that sets the target for the shooter flywheel.
     * 
     * @param target Target revolutions per minute.
     */
    public void setLowerRPMTarget(double target) {
        lowerTarget = target;
    }

    public void spinLower() {
        if (enabled) {
            // 600 since its rotation speed is in position change/100ms
            mot_lower.set(ControlMode.Velocity, lowerTarget * Constants.Falcon500.unitsPerRotation / 600.0);
        }
    }

    /**
     * Method that sets the target for the shooter flywheel.
     * 
     * @param target Target revolutions per minute.
     */
    public void setUpperRPMTarget(double target) {
        upperTarget = target;
    }

    public void spinUpper() {
        if (enabled) {
            // 600 since its rotation speed is in position change/100ms
            //mot_upper.set(1);
            mot_upper.set(ControlMode.Velocity, upperTarget * Constants.Falcon500.unitsPerRotation / 600.0);
        }
    }

    /**
     * Method for configuring the PID of the motor.
     * 
     * @param motor Motor to configure.
     * @param p     P constant
     * @param i     I constant
     * @param d     D constant
     * @param ff    Feedforward constant
     */
    public void configPID(WPI_TalonFX motor, double p, double i, double d, double ff) {
        motor.config_kP(0, p);
        motor.config_kI(0, i);
        motor.config_kD(0, d);
        motor.config_kF(0, ff);
    }

    /**
     * Method to configure the PID of the motor.
     * 
     * @param pidControl PID Controller
     * @param kP         P Constant
     * @param kI         I Constant
     * @param kD         D Constant
     * @param ff         Freeforward Constant
     */
    public void configPID(SparkMaxPIDController pidControl, double kP, double kI, double kD, double ff) {
        pidControl.setP(kP);
        pidControl.setI(kI);
        pidControl.setD(kD);
        pidControl.setFF(ff);
    }

    /**
     * Method for getting the RPM of the main motor.
     * 
     * @return A double representing the RPM of the motor.
     */
    public double getLowerRPM() {
        return (mot_lower.getSelectedSensorVelocity() * 600.0) / Constants.Falcon500.unitsPerRotation;
    }

    /**
     * Method for getting the RPM of the main motor.
     * 
     * @return A double representing the RPM of the motor.
     */
    public double getUpperRPM() {
        return (mot_upper.getSelectedSensorVelocity() * 600.0) / Constants.Falcon500.unitsPerRotation;
    }

    public boolean upperTargetReached() {
        return Math.abs(upperTarget - getUpperRPM()) <= Constants.ShooterFlywheel.SHOOTER_TOLERANCE;
    }

    public boolean lowerTargetReached() {
        return Math.abs(lowerTarget - getLowerRPM()) <= Constants.ShooterFlywheel.SHOOTER_TOLERANCE;
    }

    public void stopUpper() {
        mot_upper.disable();
    }

    public void stopLower() {
        mot_lower.disable();
    }

    /**
     * Method for getting if the subsystem is enabled.
     * 
     * @return True if the subsystem is enabled, false if not.
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void indexShootOn() {
        pidController.setReference(shuffleBoardFields.get("motor speed shooter").getDouble(50),
                CANSparkMax.ControlType.kVelocity);
        // indexerShooter_neo.set(1);
    }

    public void indexShootOff() {
        indexerShooter_neo.set(0);
        // pidController.setReference(0, CANSparkMax.ControlType.kVelocity);
    }

    public double getSpeedShoot() {
        return enc_shooter.getVelocity();
        // return speedShoot;
    }

    public void stopPreshooter() {
        // indexerShooter_neo.stopMotor();
        indexerShooter_neo.disable();
    }

    /**
     * Method for spinning the pre shooter to an RPM.
     * 
     * @param target Target RPM.
     */
    public void spinPreshooter(double target) {
        speedShoot = target;
        if (preshooterEnabled) {
            if (speedShoot == 0) {
                stopPreshooter();
            } else {
                pidController.setReference(speedShoot, CANSparkMax.ControlType.kVelocity);
            }
        }
    }

    public void disablePreshooter() {
        preshooterEnabled = false;
        stopPreshooter();
    }

    public void enablePreshooter() {
        preshooterEnabled = true;
        spinPreshooter(speedShoot);
    }

    public boolean preshooterReachedTarget() {
        return Math.abs(speedShoot - getSpeedShoot()) <= Constants.kIndexer.PRESHOOTER_TOLERANCE;
    }

    public void setSpeedShoot(double speed) {
        speedShoot = speed;
    }

    public boolean isPreshooterEnabled() {
        return preshooterEnabled;
    }
}