package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import frc.robot.Constants;
import frc.robot.Constants.kIndexer;

import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerProto extends SubsystemBase {

  // indexer testing motors
  protected final CANSparkMax indexerBelt_neo;

  // colour sensor
  private I2C.Port i2cPort_1 = I2C.Port.kOnboard;
  private ColorSensorV3 m_colourSensor_etr = new ColorSensorV3(i2cPort_1);
  private ColorMatch m_colorMatcher_etr = new ColorMatch();

  // colours
  private Color kBlueTarget = new Color(0.120, 0.402, 0.479);
  private Color kRedTarget = new Color(0.532, 0.330, 0.137);

  // colour detection
  private char allianceColour;
  private char detectedEntranceColour;

  // time of flights
  protected TimeOfFlight TOF_Ext;
  protected boolean isRangeValid_Ext;
  protected double getRange_Ext;


  // shuffleboard values
  HashMap<String, NetworkTableEntry> shuffleBoardFields;
  ShuffleboardTab tab;

  double speedBelt = 0;
  // double speedShoot = 0;

  private boolean indexerEnabled;
  // private boolean preshooterEnabled;

  public IndexerProto() {
    indexerEnabled = false;
    // test motor for belt on indexer prototype
    indexerBelt_neo = new CANSparkMax(kIndexer.kIndexBeltMotor, MotorType.kBrushless);
    indexerBelt_neo.setSmartCurrentLimit(20);
    indexerBelt_neo.setIdleMode(IdleMode.kBrake);
    indexerBelt_neo.burnFlash();

    // shuffleboard values for motors
    shuffleBoardFields = new HashMap<String, NetworkTableEntry>();
    tab = Shuffleboard.getTab("IndexerControls");
    ShuffleboardLayout mLayout = tab.getLayout("motor layout", BuiltInLayouts.kList);
    shuffleBoardFields.put("motor speed belt",
        mLayout.add("motor speed belt", speedBelt).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 100, "block increment", 10)).getEntry());

    shuffleBoardFields.put("current speed of belt", mLayout.add("Current belt speed", getSpeedBelt()).getEntry());

    

    shuffleBoardFields.put("current speed of shoot", mLayout.add("Current shooter speed", getSpeedBelt()).getEntry());

    // pid shuffle board values.
    ShuffleboardLayout pidTuningLayout = tab.getLayout("PID Tuning Controls", BuiltInLayouts.kList);
    shuffleBoardFields.put("P",
        pidTuningLayout.add("P Const:", Constants.kIndexer.UPPER_P).getEntry());

    shuffleBoardFields.put("I",
        pidTuningLayout.add("I Const:", Constants.kIndexer.UPPER_I).getEntry());

    shuffleBoardFields.put("D",
        pidTuningLayout.add("D Const:", Constants.kIndexer.UPPER_D).getEntry());

    shuffleBoardFields.put("F",
        pidTuningLayout.add("F Const:", Constants.kIndexer.UPPER_F).getEntry());

    shuffleBoardFields.put("change",
        pidTuningLayout.add("Change values", false).withWidget(BuiltInWidgets.kToggleButton).getEntry());

  }

  // testing the indexer.methods.
  public void indexBeltOn() {
    indexerBelt_neo.set(speedBelt);
  }

  public void setSpeedBelt(double speed) {
    speedBelt = speed;
  }

  public double getSpeedBelt() {
    return speedBelt;
  }

  /**
   * Method to configure the PID of the motor.
   * 
   * @param pidControl PID Controller
   * @param kP         P Constant
   * @param kI         I Constant
   * @param kD         D Constant
   * @param ff         Freeforward Constant
   */
  public void configPID(SparkMaxPIDController pidControl, double kP, double kI, double kD, double ff) {
    pidControl.setP(kP);
    pidControl.setI(kI);
    pidControl.setD(kD);
    pidControl.setFF(ff);
  }

  @Override
  public void periodic() {
    // setSpeedBelt(shuffleBoardFields.get("motor speed belt").getDouble(50));
    shuffleBoardFields.get("current speed of belt").setDouble(getSpeedBelt());

    // setSpeedShoot(shuffleBoardFields.get("motor speed shooter").getDouble(50));
  }

  public boolean isIndexerEnabled() {
    return indexerEnabled;
  }


  /**
   * Method for psinning the lower part of the indexer.
   * 
   * @param target Setpoint speed for the lower indexer, in range [-1.0, 1.0].
   */
  public void spinIndexer(double target) {
    speedBelt = target;
    if (indexerEnabled) {
      if (speedBelt == 0) {
        stopIndexer();
      } else {
        indexerBelt_neo.set(speedBelt);
      }
    }
  }

  public void stopIndexer() {
    // indexerBelt_neo.stopMotor();
    indexerBelt_neo.disable();
  }


  public void disableIndexer() {
    indexerEnabled = false;
    stopIndexer();
  }

  public void enableIndexer() {
    indexerEnabled = true;
    spinIndexer(speedBelt);
  }

   // to get colour value (entrance colour sensor)
   public void entranceColourTest() {

    final Color detectedColour = m_colourSensor_etr.getColor();
    ColorMatchResult match = m_colorMatcher_etr.matchClosestColor(detectedColour);

    final double IR = m_colourSensor_etr.getIR();
    final int proximity = m_colourSensor_etr.getProximity();

    SmartDashboard.putNumber("Entrance blue value", match.color.blue);
    SmartDashboard.putNumber("Entrance red value", match.color.red);
    SmartDashboard.putNumber("Entrance green value", match.color.green);
    SmartDashboard.putNumber("Entrance confidence", match.confidence);
    SmartDashboard.putNumber("Entrance proximity", proximity);
    SmartDashboard.putNumber("Entrance IR", IR);

  }

  public void entranceColourCalibration() {

    final Color detectedColor = m_colourSensor_etr.getColor();
    ColorMatchResult match = m_colorMatcher_etr.matchClosestColor(detectedColor);

    if (match.color == kBlueTarget) {
      detectedEntranceColour = 'B';
    } else if (match.color == kRedTarget) {
      detectedEntranceColour = 'R';
    } else {
      detectedEntranceColour = 'U';
    }
  }

  public char getEntranceColour() {
    return detectedEntranceColour;
  }

  public double getRange_Ext() {
    return TOF_Ext.getRange();
  }

  public boolean ballDetectionExit() {
    double range = TOF_Ext.getRange();

    if (range < 24) { //need to find number to compare with
      return true;
    }
    return false;
  }

  public boolean isRangeValid_Ext() {
    return TOF_Ext.isRangeValid();
  }

  public void setRangingMode(TimeOfFlight.RangingMode rangeModeIn, double sampleTime) {
    if (sampleTime > 24) {
      sampleTime = 24;
      // TOF_Ent.setRangingMode(rangeModeIn, sampleTime);
    }
  }
}
