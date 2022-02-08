package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class TestIndexProto extends CommandBase{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private IndexerProto sys_indexer;
    private ShooterFlywheel sys_flywheel;

    public TestIndexProto(IndexerProto subsystem, ShooterFlywheel flywheel){
        sys_indexer = subsystem; 
        sys_flywheel = flywheel;

        addRequirements(subsystem, flywheel);
    }

    @Override
    public void initialize(){}

    @Override
    public void execute(){
        sys_flywheel.indexShootOn();
        sys_indexer.indexBeltOn();
    }

    @Override
    public void end(boolean interuppted){}

    @Override
    public boolean isFinished(){return false;}
    
    
}