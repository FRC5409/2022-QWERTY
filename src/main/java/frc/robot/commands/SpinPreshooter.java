package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class SpinPreshooter extends CommandBase {
    double targetRPM;
    IndexerProto m_indexer;

    public SpinPreshooter(IndexerProto subsystem, double rpm){
        m_indexer = subsystem;
        targetRPM = rpm;
        addRequirements(m_indexer);
    }

    @Override
    public void execute() {
        //m_indexer.stopPreshooter();
        //if(targetRPM != 0){
            m_indexer.spinPreshooter(targetRPM);
        //}
    }

    @Override
    public boolean isFinished() {
        return m_indexer.preshooterReachedTarget() || !m_indexer.isPreshooterEnabled();
    }
    
    public double getTargetRpm(){
        return targetRPM;
    }

    public void setTargetRpm(double newTarget){
        targetRPM = newTarget;
    }
}
