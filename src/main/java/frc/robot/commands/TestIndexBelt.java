package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class TestIndexBelt extends CommandBase{

    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private Indexer sys_indexer;

    public TestIndexBelt(Indexer subsystem){
        sys_indexer = subsystem; 

        addRequirements(subsystem);
    }

    @Override 
    public void initialize(){}

    @Override
    public void execute(){
        sys_indexer.indexBeltOn();
    }

    @Override
    public void end(boolean interuppted){}

    @Override
    public boolean isFinished(){return false;}
    
}
