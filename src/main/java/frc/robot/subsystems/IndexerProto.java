package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import frc.robot.Constants;
import frc.robot.Constants.kIndexer;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerProto extends SubsystemBase {

  // indexer testing motors
  protected final CANSparkMax indexerBelt_neo;
  protected final CANSparkMax indexerShooter_neo;
  protected RelativeEncoder enc_shooter;

  // pid controller
  private SparkMaxPIDController pidController;


  // shuffleboard values
  HashMap<String, NetworkTableEntry> shuffleBoardFields;
  ShuffleboardTab tab;

  double speedBelt = 0;
  double speedShoot = 0;

  private boolean enabled;

  public IndexerProto() {
    enabled = false;
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
    shuffleBoardFields = new HashMap<String, NetworkTableEntry>();
    tab = Shuffleboard.getTab("Motors");
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

  public void indexShootOn() {
    enabled = true;
    pidController.setReference(shuffleBoardFields.get("motor speed shooter").getDouble(50),
        CANSparkMax.ControlType.kVelocity);
    // indexerShooter_neo.set(1);
  }

  public void indexShootOff() {
    enabled = false;
    indexerShooter_neo.set(0);
    // pidController.setReference(0, CANSparkMax.ControlType.kVelocity);
  }

  public void setSpeedBelt(double speed) {
    speedBelt = speed;
  }

  public void setSpeedShoot(double speed) {
    speedShoot = speed;
  }

  public double getSpeedBelt() {
    return speedBelt;
  }

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
    setSpeedBelt(shuffleBoardFields.get("motor speed belt").getDouble(50));
    shuffleBoardFields.get("current speed of belt").setDouble(getSpeedBelt());

    setSpeedShoot(shuffleBoardFields.get("motor speed shooter").getDouble(50));
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

  public boolean isEnabled() {
    return enabled;
  }
}
