package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.*;

public class AlignAndSpin extends ParallelCommandGroup {

    public AlignAndSpin(Turret turret, ShooterFlywheel flywheel, Limelight limelight){
        addCommands(new AlignTurretWithLimelight(turret, limelight), new SpinShooter(flywheel, 2200, 2200, 4500));
        
    }
    
}
