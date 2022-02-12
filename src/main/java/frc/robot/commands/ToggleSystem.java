package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class ToggleSystem extends CommandBase{

    private ShooterFlywheel flywheel;
    private IndexerProto indexer;


    public ToggleSystem(ShooterFlywheel flywheel, IndexerProto indexer){
        this.flywheel = flywheel;
        this.indexer = indexer;
        addRequirements(this.indexer, this.flywheel);
    }

    @Override
    public void execute() {
        if(flywheel.isEnabled()){
            flywheel.disable();
            flywheel.disablePreshooter();
            indexer.disableIndexer();
        } else {
            flywheel.enable();
            flywheel.enablePreshooter();
            indexer.enableIndexer();
        }        
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    
}
