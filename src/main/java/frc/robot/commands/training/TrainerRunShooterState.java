package frc.robot.commands.training;

import org.jetbrains.annotations.NotNull;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.base.StateCommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Limelight.TargetType;
import frc.robot.subsystems.shooter.ShooterFlywheel;
import frc.robot.subsystems.shooter.ShooterTurret;
import frc.robot.training.TrainerDashboard;
import frc.robot.training.TrainerContext;
import frc.robot.util.Vector2;

// TODO update doc

/**
 * <h3>Operates the turret in the "Shooting" state.</h>
 * 
 * <p>In this state, the turret runs it's flywheel at a speed 
 * porportional to the distance of the turret from the outer port
 * and aligns the turret's rotation axis to the target.
 * Once both systems have reached their respective targets,
 * the indexer triggers, feeding powercells into the turret.</p>
 */
public class TrainerRunShooterState extends StateCommandBase {
    private final ShooterFlywheel flywheel;
    private final ShooterTurret turret;
    private final Limelight limelight;
    private final Indexer indexer;
    private final TrainerDashboard dashboard;
    private final TrainerContext context;

    public TrainerRunShooterState(
        Limelight limelight,
        ShooterTurret turret,
        ShooterFlywheel flywheel,
        Indexer indexer,
        TrainerDashboard dashboard,
        TrainerContext context
    ) {
        this.limelight = limelight;
        this.flywheel = flywheel;
        this.indexer = indexer;
        this.turret = turret;
        this.dashboard = dashboard;
        this.context = context;

        addRequirements(limelight, turret, flywheel);
    }

    @Override
    public void execute() {
        Vector2 target = limelight.getTarget();
        double velocity = context.getSetpoint().getTarget();

        context.setDistance(
            Constants.Vision.DISTANCE_FUNCTION.calculate(target.y)
        );

        // Set flywheel to estimated veloctity
        flywheel.setVelocityTarget(velocity);

        // Continue aligning shooter
        if (Math.abs(target.x) > Constants.Vision.ALIGNMENT_THRESHOLD)
            turret.setRotationTarget(turret.getRotation() + target.x);

        if (turret.isTargetReached() && flywheel.isTargetReached()) {
            indexer.spinIndexer(1);
            flywheel.spinFeeder(4500);
        }

        SmartDashboard.putNumber("Active Velocity", flywheel.getVelocity());
        SmartDashboard.putNumber("Aligninment Offset", target.x);

        SmartDashboard.putNumber("Lower Velocity", flywheel.getLowerVelocity());
        SmartDashboard.putNumber("Upper Velocity", flywheel.getUpperVelocity());

        dashboard.update();
    }

    @Override
    public void end(boolean interrupted) {
        flywheel.setVelocityTarget(0);

        //TODO check if this should be disabled or should be stopped. 
        flywheel.stopFeeder();
        indexer.stopIndexer();
    }

    @Override
    public boolean isFinished() {
        return !(limelight.hasTarget() && limelight.getTargetType() == TargetType.kHub);
    }

    @Override
    public @NotNull String getStateName() {
        return "frc.robot.shooter:operate";
    }
}
