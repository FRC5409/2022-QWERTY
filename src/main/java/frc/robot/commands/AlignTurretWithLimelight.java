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
    public void execute() {
        double turnDegrees = limelight.getHorizontalOffset();
        turret.enable();
        turret.turnToAngle(turnDegrees);

    }

    @Override
    public boolean isFinished() {
        if(Math.abs(limelight.getHorizontalOffset()) < 1){
            turret.disable();
            return true;
        }
        return false;
    }
}
