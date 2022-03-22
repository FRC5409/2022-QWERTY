// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;

import java.util.HashMap;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveSwitcher extends CommandBase {

  private final DriveTrain sys_drive;
  private final XboxController m_joystick;
  private int driveMode = 1;
  private boolean forward;
  private HashMap<Integer, String> driveModeNames = new HashMap<Integer, String>();


  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public DriveSwitcher(DriveTrain subsystem, XboxController joystick, boolean forward) {
    sys_drive = subsystem;
    m_joystick = joystick;
    this.forward = forward;

    driveModeNames.put(1, "Aadil Drive");
    driveModeNames.put(2, "Tank Drive");
    driveModeNames.put(3, "Arcade Drive");
    driveModeNames.put(4, "Curv Drive");

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(sys_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (forward){
      driveMode++;      
    } else {
      driveMode--;
    }

    if (driveMode > 4) {
      driveMode = 1;
    } else if (driveMode < 1) {
      driveMode = 4;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    switch (driveModeNames.get(driveMode)) {
      case "Aadil Drive":
        aadilDriveExcecute();

      case "Tank Drive":
        tankDriveExecute();
      
      case "Arcade Drive":
        arcadeDriveExecute();

      case "Curv Drive":
        curvatureDriveExecute();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  private void aadilDriveExcecute() {
    double leftTrigger = m_joystick.getLeftTriggerAxis();
    double rightTrigger = m_joystick.getRightTriggerAxis();
    double lAxis = m_joystick.getLeftX();

    sys_drive.aadilDrive(rightTrigger, leftTrigger, lAxis);
  }

  private void tankDriveExecute() {
    double leftSpeed = m_joystick.getLeftY() * -1;
    double rightSpeed = m_joystick.getRightY() * -1;

    sys_drive.tankDrive((float) leftSpeed, (float) rightSpeed);
  }

  private void arcadeDriveExecute() {
    double acceleration = m_joystick.getLeftY() * -1;
    double turn = m_joystick.getLeftX() * -1;

    sys_drive.arcadeDrive(acceleration, turn);
  }

  /**
   * This method will get input values and run the curvDrive method.
   */
  private void curvatureDriveExecute() {
    double speed = m_joystick.getLeftY() * -1;
    double turn = m_joystick.getLeftX() * -1;

    // true if the 'b' button on the controller is pressed
    boolean quickTurn = m_joystick.getBButton();

    sys_drive.curvDrive(speed, turn, quickTurn);
  }

  /**
   * This method will get input values and run the tankDrive method.
   */


}