package frc.robot.commands;

import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.*;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;



public class AlignAndShoot extends SequentialCommandGroup{
    ShooterFlywheel flywheel;
    Limelight limelight;
    IndexerProto indexer;
    Turret turret; 
    public AlignAndShoot(Turret turret, ShooterFlywheel flywheel, Limelight limelight, IndexerProto indexer){
        addCommands(new AlignAndSpin(turret, flywheel, limelight), new SpinIndexer(indexer, 0.5));
        this.flywheel = flywheel;
        this.limelight = limelight;
        this.indexer = indexer;
        this.turret = turret;
    }

    // @Override
    // public boolean isFinished() {
    //     // TODO Auto-generated method stub
    //     return !(flywheel.isEnabled()) || !(flywheel.isPreshooterEnabled()) || !(indexer.isIndexerEnabled());
    // }


}
