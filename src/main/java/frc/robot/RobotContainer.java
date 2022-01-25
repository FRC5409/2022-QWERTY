// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.EnableShooter;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.shooter.ShooterFlywheel;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final ShooterFlywheel m_flywheel;
  //private final DriveTrain m_driveTrain;



  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  // Define main joystick
  private final XboxController joystick_main; // = new XboxController(0);
  private final JoystickButton but_main_A, but_main_B, but_main_X, but_main_Y, but_main_LBumper, but_main_RBumper,
      but_main_LAnalog, but_main_RAnalog, but_main_Back, but_main_Start;
  //private final DefaultDrive defaultDrive;
      

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    joystick_main = new XboxController(0);

    // Init button binds
    but_main_A = new JoystickButton(joystick_main, XboxController.Button.kA.value);
    but_main_B = new JoystickButton(joystick_main, XboxController.Button.kB.value);
    but_main_X = new JoystickButton(joystick_main, XboxController.Button.kX.value);
    but_main_Y = new JoystickButton(joystick_main, XboxController.Button.kY.value);
    but_main_LBumper = new JoystickButton(joystick_main, XboxController.Button.kLeftBumper.value);
    but_main_RBumper = new JoystickButton(joystick_main, XboxController.Button.kRightBumper.value);
    but_main_LAnalog = new JoystickButton(joystick_main, XboxController.Button.kLeftStick.value);
    but_main_RAnalog = new JoystickButton(joystick_main, XboxController.Button.kRightStick.value);
    but_main_Back = new JoystickButton(joystick_main, XboxController.Button.kBack.value);
    but_main_Start = new JoystickButton(joystick_main, XboxController.Button.kStart.value);

    m_flywheel = new ShooterFlywheel();
    //m_driveTrain = new DriveTrain();

    //defaultDrive = new DefaultDrive(m_driveTrain, joystick_main);
    //m_driveTrain.setDefaultCommand(defaultDrive);

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    but_main_A.whenPressed(new EnableShooter(m_flywheel));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
