package frc.robot.subsystems.shooter;

import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Class for getting data from the Limelight. 
 * 
 * @author Akil Pathiranage
 */
public class Limelight extends SubsystemBase {

    double horizontalOffset;
    double verticalOffset;
    double targetArea;
    int target;

    HashMap<String, NetworkTableEntry> data;
    HashMap<String, NetworkTableEntry> shuffleboardFields;

    NetworkTable limelightTable;

    /**
     * Constructor for the limelight object. 
     */
    public Limelight() {
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        
        horizontalOffset = 0;
        verticalOffset = 0;
        targetArea = 0;

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

    public void update(){
        if(hasTargets()){
            horizontalOffset = limelightTable.getEntry("tx").getDouble(0);
            verticalOffset =  limelightTable.getEntry("ty").getDouble(0);
            targetArea = limelightTable.getEntry("ta").getDouble(0);
        }
    }

    /**
     * Method for getting if the limelight has one or more targets. 
     * @return true if there is a or multiple targets, false if not.
     */
    public boolean hasTargets(){
        return getTargets() >= 1;
    }

    /**
     * Method for getting if the Limelight has a SINGULAR target.
     * @return true if there is one target in the limelight, false if not. 
     */
    public boolean hasTarget(){
        return getTargets() == 1;
    }

    /**
     * Method for getting the horizontal offset angle from the Limelight data table.
     * @return A double representing the offset angle from the limelight. 
     */
    public double getHorizontalOffset() {
        update();
        return horizontalOffset;
    }

    /**
     * Method for getting the vertical offset angle from the Limelight data table.
     * @return A double representing the offset angle from the limelight. 
     */
    public double getVerticalOffset() {
        update();
        return verticalOffset;
    }

    /**
     * Method for getting the target area from the Limelight table. Value from 0-1
     * @return Target area percent as a double.
     */
    public double getTargetArea() {
        update();
        return targetArea;
    }

    /**
     * Method for getting the amount of targets from the Limelight data table.
     * 
     * @return Targets the limelight has. 
     */
    public double getTargets() {
        return limelightTable.getEntry("tv").getDouble(0);
    }

    /**
     * Calculates the distance to the upper hub.
     * @return Distance to the upper hub. 
     */
    public double calculateDistanceToUpper() {
        return (2.64 - Constants.Turret.ROBOT_HEIGHT)
                / Math.tan(Math.toRadians(getVerticalOffset() + Constants.Turret.FIXED_ANGLE));
    }

    /**
     * Calculates the distance to the lower hub.
     * @return Distance to the lower hub. 
     */
    public double calculateDistanceToLower(){
        return (1.04 - Constants.Turret.ROBOT_HEIGHT)
                / Math.tan(Math.toRadians(getVerticalOffset() + Constants.Turret.FIXED_ANGLE)); 
    }

}
