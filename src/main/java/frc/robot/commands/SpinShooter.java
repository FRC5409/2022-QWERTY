package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class SpinShooter extends CommandBase {

    ShooterFlywheel flywheel;
    double preshooter, upper, lower;

    public SpinShooter(ShooterFlywheel flywheel, double upper, double lower, double preshooter) {
        this.flywheel = flywheel;
        addRequirements(this.flywheel);
        this.preshooter = preshooter;
        this.upper = upper;
        this.lower = lower;

    }

    @Override
    public void initialize() {
        flywheel.setLowerRPMTarget(lower);
        flywheel.setUpperRPMTarget(upper);
        flywheel.enablePreshooter();
        flywheel.enable();

    }
    @Override
    public void execute() {
        flywheel.spinPreshooter(preshooter);
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}
