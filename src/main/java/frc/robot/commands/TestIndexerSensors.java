package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class TestIndexerSensors extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private IndexerProto sys_indexerProto;


    int countBalls; // counts the number of cargo in the indexer

    boolean TOF_Exit; // time of flight sensor at the exit

    boolean TOF_Ball1; // time of flight sensor within the indexer

    boolean TOF_Ent; // time of flight sensor at the entrance

    char m_colourSensor_etr; // colour from sensor

    char allianceColour; // alliance colour from the FMS

    int countBalls; // counts the number of cargo in the indexer

    boolean TOF_Exit; // time of flight sensor at the exit


    public TestIndexerSensors(IndexerProto subsystem) {
        sys_indexerProto = subsystem;
        addRequirements(subsystem);
    }

    @Override

    public void initialize() {}

    @Override
    public void execute() {
        TOF_Exit = sys_indexerProto.ballDetectionExit();
        TOF_Ent = sys_indexerProto.ballDetectionEnter();
        TOF_Ball1 = sys_indexerProto.ballDetectionBall1();

        if (TOF_Ent) {
            sys_indexerProto.moveIndexerBelt(1);
            countBalls = 1;
        } else if (TOF_Ball1 && !TOF_Exit) {
            sys_indexerProto.moveIndexerBelt(0);
            countBalls = 2;
        }

        // might need this chunk of code later
        // } else if(TOF_Exit){
        //     sys_indexerProto.moveIndexerBelt(0);
        // }

        if (TOF_Ball1 == false && TOF_Exit == false && TOF_Ent == false)

    public void initialize() {
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
            sys_indexerProto.spinPreshooter(0.5);
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
