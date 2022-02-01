package frc.robot.subsystems;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.kColour;

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

        double IR = m_colourSensor.getIR();
        int proximity = m_colourSensor.getProximity();

        SmartDashboard.putNumber("Blue value", detectedColour.blue);
        SmartDashboard.putNumber("Red value", detectedColour.red);
        SmartDashboard.putNumber("Green value", detectedColour.green);
        SmartDashboard.putNumber("Proximity", proximity);
        SmartDashboard.putNumber("IR", IR);
    }

    public void initialColourCalibration(){

        final Color detectedColour = m_colourSensor.getColor();
    
        double IR = m_colourSensor.getIR(); 
        int proximity = m_colourSensor.getProximity();
    
        SmartDashboard.putNumber("Instataneous Red", detectedColour.red);
        SmartDashboard.putNumber("Instataneous Green", detectedColour.green);
        SmartDashboard.putNumber("Instataneous Blue", detectedColour.blue);
        SmartDashboard.putNumber("Instataneous Proximity", proximity);
        SmartDashboard.putNumber("Instataneous IR", IR);
    
    }

    public void colourMatch() {

        Color detectedColor = m_colourSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        int proximity = m_colourSensor.getProximity();
        String colourValue;

        if (match.color == kBlueTarget) {
            colourValue = "Blue";
        } else if (match.color == kRedTarget) {
            if (proximity > kColour.proximityThreshold){
                colourValue = "Red";
            } else{
                colourValue = "Unknown";
            }
        } else {
            colourValue = "Unknown";
        }

        SmartDashboard.putString("Detected colour", colourValue);
        SmartDashboard.putNumber("Match confidence", match.confidence);
      }

    @Override
    public void periodic() {

        colourCalibration();
        colourMatch();
    }
}
