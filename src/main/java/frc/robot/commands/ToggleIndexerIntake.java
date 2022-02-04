package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class ToggleIndexerIntake extends CommandBase{
    private IndexerProto indexer;


    public ToggleIndexerIntake(IndexerProto indexer){
        this.indexer = indexer;
        addRequirements(indexer);
    }

    @Override
    public void execute() {
        if(indexer.isIndexerEnabled()){
            indexer.disableIndexer();
        } else {
            indexer.enableIndexer();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
    
}
