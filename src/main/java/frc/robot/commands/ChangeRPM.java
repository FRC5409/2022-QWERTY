package frc.robot.commands;

import java.util.HashMap;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class ChangeRPM extends SequentialCommandGroup {

    private final boolean test;
    private SpinLowerFlywheel cmd_lowerFlywheel;
    private SpinPreshooter cmd_preshooter;
    private SpinUpperFlywheel cmd_upperFlywheel;
    private SpinIndexer cmd_spinindexer;

    private HashMap<String, NetworkTableEntry> shuffleboardEntries;

    /**
     * Shoots a ball with a specified speed for the upper flywheel, lower flywheel, preshooter speed, indexer speed. 
     * @param flywheel
     * @param indexer
     * @param upperFlywheelSpeed Speed in RPM
     * @param lowerFlywheelSpeed Speed in RPM
     * @param preshooterSpeed Speed in RPM
     * @param lowerIndexerSpeed Speed in range of [-1.0, 1.0]
     */
    public ChangeRPM(ShooterFlywheel flywheel, IndexerProto indexer, double upperFlywheelSpeed,
            double lowerFlywheelSpeed, double preshooterSpeed, double lowerIndexerSpeed) {

        test = false;
                
        addCommands(new SpinUpperFlywheel(flywheel, upperFlywheelSpeed),
                new SpinLowerFlywheel(flywheel, lowerFlywheelSpeed), new SpinPreshooter(indexer, preshooterSpeed),
                new SpinIndexer(indexer, lowerIndexerSpeed));

    }

    /**
     * Shoots a ball with a specified speed for the flywheel, speed for the preshooter and speed for the indexer.
     * 
     * @param flywheel
     * @param indexer Speed in RPM
     * @param flywheelSpeed Speed in RPM
     * @param preshooterSpeed Speed in RPM
     * @param indexerSpeed Speed in range of [-1.0, 1.0]
     */
    public ChangeRPM(ShooterFlywheel flywheel, IndexerProto indexer, double flywheelSpeed, double preshooterSpeed, double indexerSpeed) {
        test = false;
        addCommands(new SpinUpperFlywheel(flywheel, flywheelSpeed),
                new SpinLowerFlywheel(flywheel, flywheelSpeed), new SpinPreshooter(indexer, preshooterSpeed),
                new SpinIndexer(indexer, indexerSpeed));
    }

    /**
     * This constructor is called when you want data to be retrieved from shuffleboard. 
     * This is used for design testing.
     * @param flywheel
     * @param indexer
     */
    public ChangeRPM(ShooterFlywheel flywheel, IndexerProto indexer, HashMap<String, NetworkTableEntry> shuffleboardEntries){
        test = true;
        cmd_lowerFlywheel = new SpinLowerFlywheel(flywheel, 0);
        cmd_spinindexer = new SpinIndexer(indexer, 0);
        cmd_upperFlywheel = new SpinUpperFlywheel(flywheel, 0);
        cmd_preshooter = new SpinPreshooter(indexer, 0);
        addCommands(cmd_upperFlywheel, cmd_lowerFlywheel, cmd_preshooter, cmd_spinindexer);

        this.shuffleboardEntries = shuffleboardEntries;
    }

    @Override
    public void initialize() {
        if(test){
            cmd_lowerFlywheel.setTargetRpm(shuffleboardEntries.get("lowerFlywheelSpeed").getDouble(0));
            cmd_upperFlywheel.setTargetRpm(shuffleboardEntries.get("upperFlywheelSpeed").getDouble(0));
            cmd_preshooter.setTargetRpm(shuffleboardEntries.get("preshooterSpeed").getDouble(0));
            cmd_spinindexer.setSetpoint(shuffleboardEntries.get("indexerSpeed").getDouble(0));
        }
        super.initialize();
    }

}
