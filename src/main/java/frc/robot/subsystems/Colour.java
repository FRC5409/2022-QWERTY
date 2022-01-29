package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

public class Indexer extends SubsystemBase{
    private I2C.Port i2cPort = I2C.Port.kOnboard;
    private ColorSensorV3 m_colourSensor = new ColorSensorV3(i2cPort);
    private ColorMatch m_colorMatcher = new ColorMatch();

    public void colourCalibration() {

        final Color detectedColour = m_colourSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColour);

        final double IR = m_colourSensor.getIR();
        final int proximity = m_colourSensor.getProximity();

        SmartDashboard.putNumber("Blue value", match.color.blue);
        SmartDashboard.putNumber("Red value", match.color.red);
        SmartDashboard.putNumber("Green value", match.color.green);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putNumber("Proximity", proximity);
        SmartDashboard.putNumber("IR", IR);

    }

    public void initialColourCalibration(){

        final Color detectedColor = m_colourSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
    
        final double IR = m_colourSensor.getIR(); 
        final int proximity = m_colourSensor.getProximity();
    
        SmartDashboard.putNumber("Instataneous Red", m_colourSensor.getRed());
        SmartDashboard.putNumber("Instataneous Green", m_colourSensor.getGreen());
        SmartDashboard.putNumber("Instataneous Blue", m_colourSensor.getBlue());
        SmartDashboard.putNumber("Instataneous Confidence", match.confidence);
        SmartDashboard.putNumber("Instataneous Proximity", proximity);
        SmartDashboard.putNumber("Instataneous IR", IR);
    
      }

      @Override
    public void periodic() {

        colourCalibration();
    }
}
