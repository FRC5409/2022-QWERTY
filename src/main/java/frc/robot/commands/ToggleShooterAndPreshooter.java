package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class ToggleShooterAndPreshooter extends CommandBase{

    private ShooterFlywheel flywheel;
    private IndexerProto indexer;

    public ToggleShooterAndPreshooter(ShooterFlywheel flywheel, IndexerProto indexer){
        this.flywheel = flywheel;
        this.indexer = indexer;
        addRequirements(this.flywheel, this.indexer);
    }
    

    @Override
    public void execute() {
        if(flywheel.isEnabled()){
            flywheel.disable();
            indexer.disablePreshooter();
        } else {
            flywheel.enable();
            indexer.enablePreshooter();
        }        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
