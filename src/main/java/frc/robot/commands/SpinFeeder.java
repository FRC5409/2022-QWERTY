package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class SpinFeeder extends CommandBase {
    double targetRPM;
    ShooterFlywheel m_flywheel;

    public SpinFeeder(ShooterFlywheel subsystem, double rpm){
        m_flywheel = subsystem;
        targetRPM = rpm;
        addRequirements(m_flywheel);
    }

    @Override
    public void execute() {
        m_flywheel.spinFeeder(targetRPM);
    }

    @Override
    public boolean isFinished() {
        return m_flywheel.feederReachedTarget() || !m_flywheel.isEnabled();
    }

}
