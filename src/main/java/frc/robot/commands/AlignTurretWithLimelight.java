package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Limelight;
import frc.robot.subsystems.shooter.Turret;

public class AlignTurretWithLimelight extends CommandBase{

    Turret turret;
    Limelight limelight;

    public AlignTurretWithLimelight(Turret turret, Limelight limelight){
        this.turret = turret;
        this.limelight = limelight;
        addRequirements(this.turret, this.limelight);
    }


    @Override
    public void initialize() {
        double turnDegrees = limelight.getHorizontalOffset();
        turret.enable();
        turret.turnToAngle(turnDegrees);
    }
    @Override
    public void execute() {
        double turnDegrees = limelight.getHorizontalOffset();
        turret.enable();
        turret.turnToAngle(turnDegrees);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(limelight.getHorizontalOffset()) <= 0.1;
    }
    
    @Override
    public void end(boolean interrupted) {
        turret.disable();
    }
}
