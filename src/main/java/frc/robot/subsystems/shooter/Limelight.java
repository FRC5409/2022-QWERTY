package frc.robot.subsystems.shooter;

import java.util.HashMap;

import javax.naming.LimitExceededException;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Limelight extends SubsystemBase {

    double horizontalOffset;
    double verticalOffset;
    double targetArea;
    int target;

    HashMap<String, NetworkTableEntry> data;
    HashMap<String, NetworkTableEntry> shuffleboardFields;

    NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    public Limelight() {
        data = new HashMap<String, NetworkTableEntry>();
        shuffleboardFields = new HashMap<String, NetworkTableEntry>();
        data.put("horizontalOffset", limelightTable.getEntry("tx"));
        data.put("verticalOffset", limelightTable.getEntry("ty"));
        data.put("targetArea", limelightTable.getEntry("ta"));
        data.put("targets", limelightTable.getEntry("tv"));

        ShuffleboardLayout layout = Shuffleboard.getTab("Limelight").getLayout("Limelight Data", BuiltInLayouts.kList);
        shuffleboardFields.put("x", layout.add("Horizontal Offset", 0).getEntry());
        shuffleboardFields.put("y", layout.add("Vertical Offset", 0).getEntry());
        shuffleboardFields.put("a", layout.add("Target Area", 0).getEntry());
        shuffleboardFields.put("t", layout.add("Targets", 0).getEntry());
    }

    @Override
    public void periodic() {
        shuffleboardFields.get("x").setDouble(getHorizontalOffset());
        shuffleboardFields.get("y").setDouble(getVerticalOffset());
        shuffleboardFields.get("a").setDouble(getTargetArea());
        shuffleboardFields.get("t").setDouble(getTargets());
    }

    public double getHorizontalOffset() {
        //return limelightTable.getEntry("tx").getDouble(0);
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        //return data.get("horizontalOffset").getDouble(0);
    }

    public double getVerticalOffset() {
        //return limelightTable.getEntry("ty").getDouble(0);
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        //return data.get("verticalOffset").getDouble(0);
    }

    public double getTargetArea() {
        //System.out.println(limelightTable.getEntry("ta").getDouble(0));
        //return limelightTable.getEntry("ta").getDouble(0);
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        //return data.get("targetArea").getDouble(0);
    }

    public double getTargets() {
        return data.get("targets").getDouble(0);
    }

    public double calculateDistanceToUpper() {
        return (2.64 - Constants.Turret.ROBOT_HEIGHT)
                / Math.tan(Math.toRadians(getVerticalOffset() + Constants.Turret.FIXED_ANGLE));
    }

    public double calculateDistanceToLower(){
        return (1.04 - Constants.Turret.ROBOT_HEIGHT)
                / Math.tan(Math.toRadians(getVerticalOffset() + Constants.Turret.FIXED_ANGLE)); 
    }

}
