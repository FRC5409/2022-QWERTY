package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

public class Colour extends SubsystemBase{
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colourSensor = new ColorSensorV3(i2cPort);
    private ColorMatch m_colorMatcher = new ColorMatch();

    private final Color kBlueTarget = new Color(0.144043, 0.376343, 0.479980);
    private final Color kRedTarget = new Color(0.525838, 0.349365, 0.125163);

    public Colour(){
        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kRedTarget);

    }

    public void colourCalibration() {

        final Color detectedColour = m_colourSensor.getColor();
        // ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColour);

        final double IR = m_colourSensor.getIR();
        final int proximity = m_colourSensor.getProximity();

        SmartDashboard.putNumber("Blue value", detectedColour.blue);
        SmartDashboard.putNumber("Red value", detectedColour.red);
        SmartDashboard.putNumber("Green value", detectedColour.green);
        // SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putNumber("Proximity", proximity);
        SmartDashboard.putNumber("IR", IR);
    }

    public void initialColourCalibration(){

        final Color detectedColour = m_colourSensor.getColor();
        // ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColour);
    
        final double IR = m_colourSensor.getIR(); 
        final int proximity = m_colourSensor.getProximity();
    
        SmartDashboard.putNumber("Instataneous Red", detectedColour.red);
        SmartDashboard.putNumber("Instataneous Green", detectedColour.green);
        SmartDashboard.putNumber("Instataneous Blue", detectedColour.blue);
        // SmartDashboard.putNumber("Instataneous Confidence", match.confidence);
        SmartDashboard.putNumber("Instataneous Proximity", proximity);
        SmartDashboard.putNumber("Instataneous IR", IR);
    
    }

    public void colourMatch() {

        final Color detectedColor = m_colourSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
    
        if (match.color == kBlueTarget) {
            SmartDashboard.putString("Detected colour", "Blue");
        } else if (match.color == kRedTarget) {
            SmartDashboard.putString("Detected colour", "Red");
        } else {
            SmartDashboard.putString("Detected colour", "Unknown");
        }
      }

    @Override
    public void periodic() {

        colourCalibration();
        colourMatch();
    }
}
