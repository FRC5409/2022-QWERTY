package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
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

    ShuffleboardLayout flywheelLayout;
    HashMap<String, NetworkTableEntry> shuffleBoardFields;

    /**
     * Constructs a Turret Flywheel subsystem.
     */
    public ShooterFlywheel() {
        mot_main = new WPI_TalonFX(Constants.ShooterFlywheel.MAIN_MOTOR_ID);
        mot_follower = new WPI_TalonFX(Constants.ShooterFlywheel.FOLLOWER_MOTOR_ID);
        mot_follower.setInverted(true);

        mot_main.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);
        mot_main.config_kP(0, Constants.ShooterFlywheel.p);
        mot_main.config_kI(0, Constants.ShooterFlywheel.i);
        mot_main.config_kD(0, Constants.ShooterFlywheel.d);
        mot_main.config_kF(0, Constants.ShooterFlywheel.ff);

        enabled = false;
        rpmTarget = 0;

        shuffleBoardFields = new HashMap<String, NetworkTableEntry>();

        flywheelLayout = Shuffleboard.getTab("Flywheel").getLayout("flywheelLayout", BuiltInLayouts.kList);
        shuffleBoardFields.put("targetRPM",
                flywheelLayout.add("RPM target", rpmTarget).withWidget(BuiltInWidgets.kNumberSlider)
                        .withProperties(Map.of("min", 0, "max", 1000, "blockincrement", 25)).getEntry());
        shuffleBoardFields.put("currentRPM", flywheelLayout.add("Current RPM", getRPM()).getEntry());
        shuffleBoardFields.put("subsystemEnabled", flywheelLayout.add("Subsystem Enabled: ", enabled).getEntry());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {
        setRPMTarget(shuffleBoardFields.get("targetRPM").getDouble(50));
        //shuffleBoardFields.get("targetRPM").setDouble(rpmTarget);
        shuffleBoardFields.get("currentRPM").setDouble(getRPM());
        shuffleBoardFields.get("subsystemEnabled").setBoolean(enabled);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void simulationPeriodic() {
        setRPMTarget(shuffleBoardFields.get("targetRPM").getDouble(50));
        //shuffleBoardFields.get("targetRPM").setDouble(rpmTarget);
        shuffleBoardFields.get("currentRPM").setDouble(getRPM());
        shuffleBoardFields.get("subsystemEnabled").setBoolean(enabled);
        //System.out.println("periodic");
    }

    public void enable() {
        enabled = true;
        setRPMTarget(rpmTarget);
    }

    public void disable() {
        mot_main.set(ControlMode.Velocity, 0);
        enabled = false;
    }

    public void setRPMTarget(double target) {
        if (enabled) {
            rpmTarget = target;
            // 600 since its rotation speed is in position change/100ms
            mot_main.set(ControlMode.Velocity, rpmTarget * Constants.Falcon500.unitsPerRotation / 600.0);
        }
    }

    public boolean isRPMTargetReached() {
        return Math.abs(getRPM() - rpmTarget) <= Constants.ShooterFlywheel.rpmTolerance;
    }

    public double getRPM() {
        return mot_main.getSelectedSensorVelocity() * 600.0 / Constants.Falcon500.unitsPerRotation;
    }

    public double getRPMTarget() {
        return rpmTarget;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void incrementRPM() {
        rpmTarget += 50;
        setRPMTarget(rpmTarget);
    }

    public void reduceRPM() {
        if (rpmTarget > 50) {
            rpmTarget -= 50;
            setRPMTarget(rpmTarget);
        }
    }

}