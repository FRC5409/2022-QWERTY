package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Turret;

public class EnableTurret extends CommandBase{

    Turret turret;

    public EnableTurret(Turret subsystem){
        turret = subsystem;
        addRequirements(turret);
    }

    @Override
    public void execute() {
        System.out.println("Disabling");
        System.out.println("Target: " + turret.getRotationTarget());
        if(turret.isEnabled()){
            turret.disable();
        } else{
            turret.enable();
        }
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
    
}
