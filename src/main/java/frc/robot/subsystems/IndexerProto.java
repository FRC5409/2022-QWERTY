package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;
import frc.robot.Constants.kIndexer;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
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

  // pid controller
  private SparkMaxPIDController pidController;

  // pid values
  public double kP, kI, kD, ff;

  // shuffleboard values
  HashMap<String, NetworkTableEntry> shuffleBoardFields;
  ShuffleboardTab tab;

  double speedBelt = 0;
  double speedShoot = 0;

  public IndexerProto() {
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
            .withProperties(Map.of("min", 0, "max", 1, "block increment", 0.1)).getEntry());

    shuffleBoardFields.put("current speed of shoot", mLayout.add("Current shooter speed", getSpeedBelt()).getEntry());

    // pid shuffle board values.
    ShuffleboardLayout pidTuningLayout = tab.getLayout("PID Tuning Controls", BuiltInLayouts.kList);
    shuffleBoardFields.put("P",
        pidTuningLayout.add("P Const:", kP).getEntry());

    shuffleBoardFields.put("I",
        pidTuningLayout.add("I Const:", kI).getEntry());

    shuffleBoardFields.put("D",
        pidTuningLayout.add("D Const:", kD).getEntry());

    shuffleBoardFields.put("change",
        pidTuningLayout.add("Change values", false).withWidget(BuiltInWidgets.kToggleButton).getEntry());

  }

  // testing the indexer.methods.
  public void indexBeltOn() {
    indexerBelt_neo.set(speedBelt);
  }

  public void indexShootOn() {
    indexerShooter_neo.set(speedShoot);
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
    return speedShoot;
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
      configPID(pidController, shuffleBoardFields.get("P").getDouble(0), shuffleBoardFields.get("I").getDouble(0), shuffleBoardFields.get("D").getDouble(0),
          shuffleBoardFields.get("ff").getDouble(0));

      System.out.println("PID Configured");
      shuffleBoardFields.get("change").setBoolean(false);
    }

  }
}
