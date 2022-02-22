package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class SpinIndexer extends CommandBase{

    double setpoint;
    Indexer m_indexer;


    public SpinIndexer(Indexer subsystem, double setpoint){
        m_indexer = subsystem;
        this.setpoint = setpoint;
        addRequirements(m_indexer);
    }

    @Override
    public void initialize() {
        m_indexer.enable();
    }

    @Override
    public void execute() {
        m_indexer.spinIndexer(setpoint);
    }

    @Override
    public void end(boolean interrupted) {
        m_indexer.stopIndexer();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public double getSetpoint(){
        return setpoint;
    }

    public void setSetpoint(double newSetpoint){
        setpoint = newSetpoint;
    }
    
}
