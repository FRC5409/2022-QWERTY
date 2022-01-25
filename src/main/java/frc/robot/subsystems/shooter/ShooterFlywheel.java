package frc.robot.subsystems.shooter;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;

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

    WPI_TalonFX mot_main;
    WPI_TalonFX mot_follower;
    double rpmTarget;
    boolean enabled;

    ShuffleboardTab tab;
    ShuffleboardLayout flywheelLayout;
    HashMap<String, NetworkTableEntry> shuffleBoardFields;

    /**
     * Constructs a flywheel subsystem.
     */
    public ShooterFlywheel() {
        mot_main = new WPI_TalonFX(Constants.ShooterFlywheel.MAIN_MOTOR_ID);
        mot_follower = new WPI_TalonFX(Constants.ShooterFlywheel.FOLLOWER_MOTOR_ID);

        mot_main.configFactoryDefault();
        mot_follower.configFactoryDefault();

        mot_follower.setInverted(true);
        mot_main.setNeutralMode(NeutralMode.Coast);
        mot_follower.setNeutralMode(NeutralMode.Coast);

        mot_main.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);
        configPID(Constants.ShooterFlywheel.p, Constants.ShooterFlywheel.i, Constants.ShooterFlywheel.d,
                Constants.ShooterFlywheel.ff);
        
        enabled = false;
        rpmTarget = 0;

        mot_follower.follow(mot_main);


        //shuffleboard data initialization
        //this data is updated in periodic of this subsystem

        shuffleBoardFields = new HashMap<String, NetworkTableEntry>();

        tab = Shuffleboard.getTab("Flywheel");
        flywheelLayout = tab.getLayout("flywheelLayout", BuiltInLayouts.kList);
        shuffleBoardFields.put("targetRPM",
                flywheelLayout.add("RPM target", rpmTarget).withWidget(BuiltInWidgets.kNumberSlider)
                        .withProperties(Map.of("min", 0, "max", 6000, "blockincrement", 100)).getEntry());
        shuffleBoardFields.put("currentRPM", flywheelLayout.add("Current RPM", getRPM()).getEntry());
        shuffleBoardFields.put("subsystemEnabled", flywheelLayout.add("Subsystem Enabled: ", enabled).getEntry());
        shuffleBoardFields.put("fluctuationGraph",
                flywheelLayout.add("RPM Fluctuation Graph", getRPM() - getRPMTarget()).withWidget(BuiltInWidgets.kGraph)
                        .withProperties(Map.of("visibletime", 10)).getEntry());
        

        ShuffleboardLayout pidTuningLayout = tab.getLayout("PID Tuning Controls", BuiltInLayouts.kList);
        shuffleBoardFields.put("p",pidTuningLayout.add("P Const: ", Constants.ShooterFlywheel.p).getEntry());
        shuffleBoardFields.put("i", pidTuningLayout.add("I const: ", Constants.ShooterFlywheel.i).getEntry());
        shuffleBoardFields.put("d", pidTuningLayout.add("D Const: ", Constants.ShooterFlywheel.d).getEntry());
        shuffleBoardFields.put("f", pidTuningLayout.add("F const: ", Constants.ShooterFlywheel.ff).getEntry());
        shuffleBoardFields.put("change", pidTuningLayout.add("Change values", false).withWidget(BuiltInWidgets.kToggleButton).getEntry());
        shuffleBoardFields.put("graph", pidTuningLayout.add("Current vs Time", 0.0).withWidget(BuiltInWidgets.kGraph).withProperties(Map.of("visibletime", 10)).getEntry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {
        setRPMTarget(shuffleBoardFields.get("targetRPM").getDouble(50));
        shuffleBoardFields.get("currentRPM").setDouble(getRPM());
        shuffleBoardFields.get("subsystemEnabled").setBoolean(enabled);
        shuffleBoardFields.get("fluctuationGraph").setDouble(getRPM() - getRPMTarget());
        shuffleBoardFields.get("graph").setDouble(mot_main.getStatorCurrent());
        if(shuffleBoardFields.get("change").getBoolean(false)){
            disable();
            mot_main.config_kP(0, shuffleBoardFields.get("p").getDouble(0));
            mot_main.config_kI(0, shuffleBoardFields.get("i").getDouble(0));
            mot_main.config_kD(0, shuffleBoardFields.get("d").getDouble(0));
            mot_main.config_kF(0, shuffleBoardFields.get("f").getDouble(0));
            System.out.println("Pid configured");
            enable();
            shuffleBoardFields.get("change").setBoolean(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void simulationPeriodic() {
        setRPMTarget(shuffleBoardFields.get("targetRPM").getDouble(50));
        shuffleBoardFields.get("currentRPM").setDouble(getRPM());
        shuffleBoardFields.get("subsystemEnabled").setBoolean(enabled);
        shuffleBoardFields.get("fluctuationGraph").setDouble(getRPM() - getRPMTarget());
        shuffleBoardFields.get("graph").setDouble(mot_main.getStatorCurrent());
        if(shuffleBoardFields.get("change").getBoolean(false)){
            disable();
            mot_main.config_kP(0, shuffleBoardFields.get("p").getDouble(0));
            mot_main.config_kI(0, shuffleBoardFields.get("i").getDouble(0));
            mot_main.config_kD(0, shuffleBoardFields.get("d").getDouble(0));
            mot_main.config_kF(0, shuffleBoardFields.get("f").getDouble(0));
            System.out.println("Pid configured");
            enable();
            shuffleBoardFields.get("change").setBoolean(false);
        }
    }

    /**
     * Method for enabing the flywheel.
     */
    public void enable() {
        enabled = true;
        setRPMTarget(rpmTarget);
    }

    /**
     * Method for disabling the flywheel.
     */
    public void disable() {
        enabled = false;
        mot_main.set(0);
        //mot_main.set(ControlMode.Velocity, 0);
    }

    /**
     * Method that sets the target for the shooter flywheel.
     * 
     * @param target Target revolutions per minute.
     */
    public void setRPMTarget(double target) {
        if (enabled) {
            rpmTarget = target;
            // 600 since its rotation speed is in position change/100ms
            mot_main.set(ControlMode.Velocity, rpmTarget * Constants.Falcon500.unitsPerRotation / 600.0);
        }
    }

    /**
     * Method for configuring the PID of the main motor.
     * 
     * @param p  P constant
     * @param i  I constant
     * @param d  D constant
     * @param ff Feedforward constant
     */
    public void configPID(double p, double i, double d, double ff) {
        mot_main.config_kP(0, Constants.ShooterFlywheel.p);
        mot_main.config_kI(0, Constants.ShooterFlywheel.i);
        mot_main.config_kD(0, Constants.ShooterFlywheel.d);
        mot_main.config_kF(0, Constants.ShooterFlywheel.ff);
    }

    /**
     * Method for getting the RPM of the main motor.
     * 
     * @return A double representing the RPM of the motor.
     */
    public double getRPM() {
        return (mot_main.getSelectedSensorVelocity() * 600.0) / Constants.Falcon500.unitsPerRotation;
    }

    /**
     * Method for getting the current RPMTarget.
     * 
     * @return A double representing the target RPM.
     */
    public double getRPMTarget() {
        return rpmTarget;
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