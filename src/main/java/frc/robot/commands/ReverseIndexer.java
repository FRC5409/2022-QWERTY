package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class ReverseIndexer extends CommandBase{
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private IndexerProto sys_indexerProto;

    public ReverseIndexer(IndexerProto subsystem){
        sys_indexerProto = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void execute(){
        sys_indexerProto.moveIndexerBelt(-0.5);
    }
    
}
