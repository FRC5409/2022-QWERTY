package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;
import frc.robot.subsystems.shooter.ShooterFlywheel;

public class TestIndexerSensors extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private IndexerProto sys_indexerProto;
    private ShooterFlywheel sys_flywheel;


    int countBalls; // counts the number of cargo in the indexer

    boolean TOF_Exit; // time of flight sensor at the exit


    boolean TOF_Ball1; // time of flight sensor within the indexer

    boolean TOF_Ent; // time of flight sensor at the entrance

    char m_colourSensor_etr; // colour from sensor

    char allianceColour; // alliance colour from the FMS


    public TestIndexerSensors(IndexerProto subsystem) {
        sys_indexerProto = subsystem;
        sys_flywheel = flywheel;

        addRequirements(subsystem, flywheel);
    }

    @Override
    public void execute() {
        m_colourSensor_etr = sys_indexerProto.getEntranceColour();
        // allianceColour = sys_indexerProto.getFMS();
        TOF_Exit = sys_indexerProto.ballDetectionExit();

        if (m_colourSensor_etr == 'B' || m_colourSensor_etr == 'R' && TOF_Exit == false) {
            sys_indexerProto.spinIndexer(0.5);
            countBalls++;
        } else if (m_colourSensor_etr == 'B' || m_colourSensor_etr == 'R' && TOF_Exit == true) {
            sys_indexerProto.spinIndexer(0);
            sys_flywheel.spinPreshooter(0.5);
            countBalls++;
        }

        if (m_colourSensor_etr != 'B' || m_colourSensor_etr == 'R' && TOF_Exit == false)
            countBalls = 0;

        System.out.println(countBalls);

    }

    @Override
    public void end(boolean interuppted) {

        sys_indexerProto.moveIndexerBelt(0);
        //reverse intake. 

    }

    @Override
    public boolean isFinished() {
        return sys_indexerProto.ballDetectionExit() && sys_indexerProto.isRangeValid_Ext();

    }

}
