// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DefaultDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain sys_drive;
  private final XboxController m_joystick;

  /**
   * Creates a new DefaultDrive
   *.
   *
   * @param subsystem The subsystem used by this command.
   * @param joystick The input device used by this command.
   */
  public DefaultDrive(DriveTrain subsystem, XboxController joystick) {
    sys_drive = subsystem;
    m_joystick = joystick;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }
 
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      // Case to determine what control scheme to utilize 
      aadilDriveExecute();
  }

  /**
   * This method runs the robot with the aadilDrive control scheme
   */
  private void aadilDriveExecute(){
    double leftTrigger = m_joystick.getLeftTriggerAxis();
    double rightTrigger = m_joystick.getRightTriggerAxis();
    double lxAxis = m_joystick.getLeftX() * -1;

    sys_drive.aadilDrive(rightTrigger, leftTrigger, lxAxis);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
