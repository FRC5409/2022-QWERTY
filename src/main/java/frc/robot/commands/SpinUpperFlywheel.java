package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class SpinUpperFlywheel extends CommandBase{

    private double targetRPM;
    private ShooterFlywheel sys_flywheel;

    public SpinUpperFlywheel(ShooterFlywheel subsystem, double rpm){
        sys_flywheel = subsystem;
        addRequirements(sys_flywheel);
        targetRPM = rpm;

    }

    @Override
    public void execute() {
        sys_flywheel.setUpperRPMTarget(targetRPM);
    }

    @Override
    public boolean isFinished() {
        return sys_flywheel.upperTargetReached() || !sys_flywheel.isEnabled();
    }

    public double getTargetRpm(){
        return targetRPM;
    }

    public void setTargetRpm(double newTarget){
        targetRPM = newTarget;
    }


    
}
