package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class SpinPreshooter extends CommandBase {
    double targetRPM;
    ShooterFlywheel m_flywheel;

    public SpinPreshooter(ShooterFlywheel shooterFlywheel, double rpm){
        m_flywheel = shooterFlywheel;

        targetRPM = rpm;
        addRequirements(m_flywheel);
    }

    @Override
    public void execute() {
        //m_indexer.stopPreshooter();
        //if(targetRPM != 0){
            m_flywheel.spinPreshooter(targetRPM);
        //}
    }

    @Override
    public boolean isFinished() {
        return m_flywheel.preshooterReachedTarget() || !m_flywheel.isPreshooterEnabled();
    }
    
    public double getTargetRpm(){
        return targetRPM;
    }

    public void setTargetRpm(double newTarget){
        targetRPM = newTarget;
    }
}
