package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class TestIndexerSensors extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private IndexerProto sys_indexerProto;

    char m_colourSensor_etr; // colour from sensor

    char allianceColour; // alliance colour from the FMS

    int countBalls; // counts the number of cargo in the indexer

    boolean TOF_Exit; // time of flight sensor at the exit

    boolean TOF_Ent; // time of flight sensor at the entrance

    public TestIndexerSensors(IndexerProto subsystem) {
        sys_indexerProto = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        m_colourSensor_etr = sys_indexerProto.getEntranceColour();
        // allianceColour = sys_indexerProto.getFMS();
        TOF_Exit = sys_indexerProto.ballDetectionExit();
        TOF_Ent = sys_indexerProto.ballDetectionEnter();

        // if (m_colourSensor_etr == 'B' || m_colourSensor_etr == 'R' && TOF_Ent == false) {
        //     sys_indexerProto.spinIndexer(0.5);
        //     countBalls++;
        // } else if (m_colourSensor_etr == 'B' || m_colourSensor_etr == 'R' && TOF_Ent == true) {
        //     sys_indexerProto.spinIndexer(0.5);
        //     countBalls++;
        // } else if(TOF_Exit){
        //     sys_indexerProto.spinIndexer(0);
        // }

        if(m_colourSensor_etr == 'B' || m_colourSensor_etr == 'R'){
            //move indexer motor
            countBalls = 1;
        } else if(TOF_Ent && !TOF_Exit){
            //stop indexer motor
            countBalls = 2; 
        }

        if (m_colourSensor_etr != 'B' || m_colourSensor_etr == 'R' && TOF_Exit == false && TOF_Ent == false)
            countBalls = 0;

        System.out.println(countBalls);

    }

    @Override
    public void end(boolean interuppted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
