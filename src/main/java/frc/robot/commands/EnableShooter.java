package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class EnableShooter extends CommandBase{

    ShooterFlywheel flywheel;

    public EnableShooter(ShooterFlywheel subsystem){
        flywheel = subsystem;
        addRequirements(flywheel);
    }

    @Override
    public void execute(){
        if(flywheel.isEnabled()){
            flywheel.disable();
        } else {
            flywheel.enable();
        }
        //System.out.println("changed");
    }
    
    @Override
    public boolean isFinished(){
        return true;
    }
}
