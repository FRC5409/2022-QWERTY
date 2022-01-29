/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Colour;

public class TuningTesting extends CommandBase {

  private Colour sys_indexer;
  /**
   * Creates a new TuningTesting.
   */
  public TuningTesting(Colour subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.

    sys_indexer = subsystem;
    addRequirements(sys_indexer);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    sys_indexer.initialColourCalibration();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}