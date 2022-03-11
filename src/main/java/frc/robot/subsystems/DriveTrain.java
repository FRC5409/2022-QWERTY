// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.kDrive;

public class DriveTrain extends SubsystemBase {

  private final WPI_TalonSRX mot_rightFrontDrive;
  private final WPI_TalonSRX mot_rightRearDrive;
  private final WPI_TalonSRX mot_leftFrontDrive;
  private final WPI_TalonSRX mot_leftRearDrive;
  private DifferentialDrive m_drive;
  private String drive_state;

  /** Creates a new ExampleSubsystem. */
  public DriveTrain() {

    mot_rightFrontDrive = new WPI_TalonSRX(51);
    mot_rightRearDrive = new WPI_TalonSRX(52);

    mot_rightRearDrive.follow(mot_rightFrontDrive);

    mot_rightFrontDrive.setInverted(true);
    mot_rightRearDrive.setInverted(true);

    mot_leftFrontDrive = new WPI_TalonSRX(53);
    mot_leftRearDrive = new WPI_TalonSRX(54);

    mot_leftRearDrive.follow(mot_leftFrontDrive);

    m_drive = new DifferentialDrive(mot_rightFrontDrive, mot_leftFrontDrive);

    drive_state = "";


  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void aadilDrive(final double acceleration, final double deceleration,  final double turn) {
    double accelerate = acceleration - deceleration;
    
    if (accelerate > 0 && turn == 0 && drive_state != "forward") {
      drive_state = "forward";
      setRampRate(kDrive.forwardRampRate);
    }

    if (accelerate < 0 && turn == 0 && drive_state != "backward") {
      drive_state = "backward";
      setRampRate(kDrive.backwardRampRate);
    }

    if (accelerate > 0 && turn != 0 && drive_state != "forward turn") {
      drive_state = "forward turn";
      setRampRate(kDrive.forwardTurnRampRate);   
    }

    if (accelerate < 0 && turn != 0 && drive_state != "backward turn") {
      drive_state = "backward turn";
      setRampRate(kDrive.backwardTurnRampRate);

    }

    m_drive.arcadeDrive(accelerate, turn, true);
  }

  public void tankDrive(final double leftStick, final double rightStick) {
    m_drive.tankDrive(leftStick, rightStick);
  }

  public void setRampRate(double rampRate) {

    mot_leftFrontDrive.configOpenloopRamp(rampRate);
    mot_rightFrontDrive.configOpenloopRamp(rampRate);

  }

}
