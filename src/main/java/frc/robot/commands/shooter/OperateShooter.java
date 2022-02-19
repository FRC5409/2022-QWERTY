package frc.robot.commands.shooter;

import frc.robot.Constants;
import frc.robot.base.StateCommandGroup;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.shooter.ShooterFlywheel;
import frc.robot.subsystems.shooter.ShooterTurret;

import frc.robot.util.*;

/**
 * This command runs the turret flywheel at a speed
 * porportional to the distance of the turret from the
 * shooting target and operates the indexer once the rpm
 * reaches it's setpoint.
 * 
 * @author Keith Davies
 */
public final class OperateShooter extends StateCommandGroup {
    private final ShooterFlywheel flywheel;
    private final ShooterTurret   turret;
    private final Limelight       limelight;
    private final IndexerProto    indexer;

    private final ShooterModel    model;

    public OperateShooter(
        Limelight limelight,
        ShooterTurret turret,
        ShooterFlywheel flywheel,
        IndexerProto indexer,
        ShooterModel model
    ) {
        this.flywheel = flywheel;
        this.turret = turret;
        this.limelight = limelight;
        this.indexer = indexer;
        this.model = model;

        addCommands(
            new SearchShooterState(limelight),
            new SweepShooterState(limelight, turret),
            new AlignShooterState(limelight, turret),
            new OperateShooterState(limelight, turret, flywheel, indexer, model)
        );
    }

    @Override
    public void initialize() {
        flywheel.enable();
        turret.enable();
        limelight.enable();
        
        flywheel.setVelocityTarget(
            model.calculate(Constants.Shooter.PRE_SHOOTER_DISTANCE)
            );

        super.initialize();
    }


    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);

        flywheel.disable();
        turret.disable();
        limelight.disable();

        indexer.stopPreshooter();
        indexer.stopIndexer();
    }
}