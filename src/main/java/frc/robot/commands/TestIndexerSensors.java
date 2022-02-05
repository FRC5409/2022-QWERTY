package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerProto;

public class TestIndexerSensors extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private IndexerProto sys_indexerProto;

    char m_colourSensor_etr;

    char allianceColour;

    int countBalls;

    boolean TOF_Exit;

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
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
