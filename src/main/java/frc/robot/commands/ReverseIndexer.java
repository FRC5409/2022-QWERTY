package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class ReverseIndexer extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private IndexerProto sys_indexerProto;

    boolean enabled;

    public ReverseIndexer(IndexerProto subsystem) {
        sys_indexerProto = subsystem;
        addRequirements(subsystem);
    }

    // @Override
    // public void initialize() {
    //     if (sys_indexerProto.getSpeedBelt() != 0) {
    //         enabled = true;
    //         sys_indexerProto.moveIndexerBelt(0);
    //     } else {
    //         enabled = false;
    //     }
    // }

    @Override
    public void execute() {
        
        sys_indexerProto.moveIndexerBelt(-0.5);
        
    }

    @Override
    public void end(boolean interuptted) {
        sys_indexerProto.moveIndexerBelt(0);
    }

}
