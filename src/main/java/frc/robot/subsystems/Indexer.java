package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

public class Indexer {
    private I2C.Port i2cPort_1 = I2C.Port.kOnboard;
    private ColorSensorV3 m_colourSensor_etr = new ColorSensorV3(i2cPort_1);
    private ColorMatch m_colorMatcher_etr = new ColorMatch();
}
