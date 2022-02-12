package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class SpinLowerFlywheel extends CommandBase{
    double targetRPM;
    ShooterFlywheel sys_flywheel;

    public SpinLowerFlywheel(ShooterFlywheel subsystem, double rpm){
        sys_flywheel = subsystem;
        addRequirements(sys_flywheel);
        targetRPM = rpm;

    }

    @Override
    public void execute() {
        sys_flywheel.setLowerRPMTarget(targetRPM);
    }

    @Override
    public boolean isFinished() {
        return sys_flywheel.lowerTargetReached() || !sys_flywheel.isEnabled();
    }

    public double getTargetRpm(){
        return targetRPM;
    }

    public void setTargetRpm(double newTarget){
        targetRPM = newTarget;
    }
}
