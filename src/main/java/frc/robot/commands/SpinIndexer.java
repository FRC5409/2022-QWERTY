package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class SpinIndexer extends CommandBase{

    double setpoint;
    IndexerProto m_indexer;


    public SpinIndexer(IndexerProto subsystem, double setpoint){
        m_indexer = subsystem;
        this.setpoint = setpoint;
        addRequirements(m_indexer);
    }

    @Override
    public void execute() {
        m_indexer.spinIndexer(setpoint);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    public double getSetpoint(){
        return setpoint;
    }

    public void setSetpoint(double newSetpoint){
        setpoint = newSetpoint;
    }
    
}
