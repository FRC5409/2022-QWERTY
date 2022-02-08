package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import frc.robot.Constants;
import frc.robot.Constants.kIndexer;

import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerProto extends SubsystemBase {

  // indexer testing motors
  protected final CANSparkMax indexerBelt_neo;
  protected final CANSparkMax indexerShooter_neo;
  protected RelativeEncoder enc_shooter;

  // pid controller
  private SparkMaxPIDController pidController;

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
  protected TimeOfFlight TOF_Ent;
  protected TimeOfFlight TOF_Ball1;
  protected boolean isRangeValid_Ball1;
  protected boolean isRangeValid_Ext;
  protected boolean isRangeValid_Ent;
  protected double getRange_Ball1;
  protected double getRange_Ext;
  protected double getRange_Ent;

  // shuffleboard values
  HashMap<String, NetworkTableEntry> shuffleBoardFields;
  ShuffleboardTab tab;

  double speedBelt = 0;
  double speedShoot = 0;

  private boolean indexerEnabled;
  private boolean preshooterEnabled;

  public IndexerProto() {
    preshooterEnabled = false;
    indexerEnabled = false;

    // MOTORS
    // --------------------------------------------------------------------------------------------

    // test motor for belt on indexer prototype
    indexerBelt_neo = new CANSparkMax(kIndexer.kIndexBeltMotor, MotorType.kBrushless);
    indexerBelt_neo.setSmartCurrentLimit(20);
    indexerBelt_neo.setIdleMode(IdleMode.kBrake);
    indexerBelt_neo.burnFlash();

    // test motor for indexer to shooter (flywheel thing)
    indexerShooter_neo = new CANSparkMax(kIndexer.kIndexShooterMotor, MotorType.kBrushless);
    indexerShooter_neo.setSmartCurrentLimit(20);
    indexerShooter_neo.setIdleMode(IdleMode.kBrake);
    indexerShooter_neo.setInverted(true);
    indexerShooter_neo.burnFlash();

    enc_shooter = indexerShooter_neo.getEncoder();

    // initialize PID controller
    pidController = indexerShooter_neo.getPIDController();

    // shuffleboard values for motors
    // -------------------------------------------------------------------
    shuffleBoardFields = new HashMap<String, NetworkTableEntry>();
    tab = Shuffleboard.getTab("IndexerControls");
    ShuffleboardLayout mLayout = tab.getLayout("motor layout", BuiltInLayouts.kList);
    shuffleBoardFields.put("motor speed belt",
        mLayout.add("motor speed belt", speedBelt).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 100, "block increment", 10)).getEntry());

    shuffleBoardFields.put("current speed of belt", mLayout.add("Current belt speed", getSpeedBelt()).getEntry());

    shuffleBoardFields.put("motor speed shooter",
        mLayout.add("motor speed shooter", speedShoot).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 5000, "block increment", 100)).getEntry());

    shuffleBoardFields.put("current speed of shoot", mLayout.add("Current shooter speed", getSpeedBelt()).getEntry());

    configPID(pidController, Constants.kIndexer.UPPER_P, Constants.kIndexer.UPPER_I, Constants.kIndexer.UPPER_D,
        Constants.kIndexer.UPPER_F);

    // pid shuffle board values.
    // ------------------------------------------------------------------------
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

  // INDEXER METHODS
  // ------------------------------------------------------------------------------------

  public void moveIndexerBelt(double speed) {
    indexerBelt_neo.set(speed);
  }

  /**
   * set the speed of the indexer belt
   */
  public void indexBeltOn() {
    indexerBelt_neo.set(speedBelt);
  }

  /**
   * set pidController
   */
  public void indexShootOn() {
    pidController.setReference(shuffleBoardFields.get("motor speed shooter").getDouble(50),
        CANSparkMax.ControlType.kVelocity);
    // indexerShooter_neo.set(1);
  }

  /**
   * turns off the pre-shooter motor
   */
  public void indexShootOff() {
    indexerShooter_neo.set(0);
    // pidController.setReference(0, CANSparkMax.ControlType.kVelocity);
  }

  /**
   * @param speed speed of belt motor
   *              sets speed of the belt motor
   */
  public void setSpeedBelt(double speed) {
    speedBelt = speed;
  }

  /**
   * @param speed speed of pre-shooter
   *              sets speed of pre-shooter motor
   */
  public void setSpeedShoot(double speed) {
    speedShoot = speed;
  }

  /**
   * @return speedBelt
   *         returns the speed of the belt motor
   */
  public double getSpeedBelt() {
    return speedBelt;
  }

  /**
   * @return enc_shooter.getVelocity()
   *         gets the velocity of the encoder
   */
  public double getSpeedShoot() {
    return enc_shooter.getVelocity();
    // return speedShoot;
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
    shuffleBoardFields.get("current speed of shoot").setDouble(getSpeedShoot());

    if (shuffleBoardFields.get("change").getBoolean(false)) {
      indexShootOff();
      configPID(pidController, shuffleBoardFields.get("P").getDouble(0), shuffleBoardFields.get("I").getDouble(0),
          shuffleBoardFields.get("D").getDouble(0),
          shuffleBoardFields.get("F").getDouble(0));

      System.out.println("PID Configured");
      shuffleBoardFields.get("change").setBoolean(false);
    }

  }

  /**
   * is the pre-shooter enabled
   * 
   * @return preshooterEnabled
   */
  public boolean isPreshooterEnabled() {
    return preshooterEnabled;
  }

  /**
   * is the indexerEnabled
   * 
   * @return indexerEnabled
   */
  public boolean isIndexerEnabled() {
    return indexerEnabled;
  }

  /**
   * stops pre-shooter motor
   */
  public void stopPreshooter() {
    // indexerShooter_neo.stopMotor();
    indexerShooter_neo.disable();
  }

  /**
   * Method for spinning the pre shooter to an RPM.
   * 
   * @param target Target RPM.
   */
  public void spinPreshooter(double target) {
    speedShoot = target;
    if (preshooterEnabled) {
      if (speedShoot == 0) {
        stopPreshooter();
      } else {
        pidController.setReference(speedShoot, CANSparkMax.ControlType.kVelocity);
      }
    }
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

  /**
   * stops indexer belt motor
   */
  public void stopIndexer() {
    // indexerBelt_neo.stopMotor();
    indexerBelt_neo.disable();
  }

  /**
   * disables pre-shooter
   */
  public void disablePreshooter() {
    preshooterEnabled = false;
    stopPreshooter();
  }

  /**
   * disables indexer
   */
  public void disableIndexer() {
    indexerEnabled = false;
    stopIndexer();
  }

  /**
   * enables preshooter
   */
  public void enablePreshooter() {
    preshooterEnabled = true;
    spinPreshooter(speedShoot);
  }

  /**
   * enables indexer
   */
  public void enableIndexer() {
    indexerEnabled = true;
    spinIndexer(speedBelt);
  }

  /**
   * 
   * @return something idk
   */
  public boolean preshooterReachedTarget() {
    return Math.abs(speedShoot - getSpeedShoot()) <= Constants.kIndexer.PRESHOOTER_TOLERANCE;
  }

  public char getEntranceColour() {
    return detectedEntranceColour;
  }

  // TIME OF FLIGHT METHODS
  // ----------------------------------------------------------------------------

  /**
   * returns the range of the TOF
   * 
   * @return TOF_Exit.getRange()
   */
  public double getRange_Ext() {
    return TOF_Ext.getRange();
  }

  /**
   * returns the range of the TOF
   * 
   * @return TOF_Ball1.getRange()
   */
  public double getRange_Ball1() {
    return TOF_Ball1.getRange();
  }

  /**
   * returns the range of the TOF
   * 
   * @return TOF_Ent.getRange()
   */
  public double getRange_Ent() {
    return TOF_Ent.getRange();
  }

  /**
   * detects whether or not the ball is in range
   * 
   * @return true/false
   */
  public boolean ballDetectionExit() {
    double range = TOF_Ext.getRange();

    if (range < 24) { // need to find number to compare with
      return true;
    }
    return false;
  }

  /**
   * detects whether or not the ball is in range
   * 
   * @return true/false
   */
  public boolean ballDetectionBall1() {
    double range = TOF_Ball1.getRange();

    if (range < 24) {
      return true;
    }

    return false;

  }

  /**
   * detects whether the ball is in range or not
   * 
   * @return true/false
   */
  public boolean ballDetectionEnter() {
    double range = TOF_Ent.getRange();

    if (range < 24) {
      return true;
    }
    return false;
  }

  /**
   * checks whether the range is valid
   * 
   * @return TOF_Exit.isRangeValid()
   */
  public boolean isRangeValid_Ext() {
    return TOF_Ext.isRangeValid();
  }

  /**
   * checks whether the range is valid
   * 
   * @return TOF_Ball1.isRangeValid()
   */
  public boolean isRangeValid_Ball1() {
    return TOF_Ball1.isRangeValid();
  }

  /**
   * checks whether the range is valid or not
   * 
   * @return TOF_Ent.isRangeValid()
   */
  public boolean isRangeValid_Ent() {
    return TOF_Ent.isRangeValid();
  }

  /**
   * sets ranging mode
   * 
   * @param rangeModeIn
   * @param sampleTime
   */
  public void setRangingMode(TimeOfFlight.RangingMode rangeModeIn, double sampleTime) {
    if (sampleTime > 24) {
      sampleTime = 24;
      TOF_Ext.setRangingMode(rangeModeIn, sampleTime);
    }
  }
}
