// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.base.Joystick;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.shooter.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private final ShooterFlywheel flywheel;
    private final Colour          colour;
    private final IndexerProto    indexer;
    private final Limelight      limelight;
    private final ShooterTurret   turret;


    // Define main joystick
    private final Joystick        joystick_main;

    HashMap<String, NetworkTableEntry> shuffleboard;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        joystick_main = new Joystick(0);

        colour = new Colour();
        flywheel = new ShooterFlywheel();
        indexer = new IndexerProto();
        turret = new ShooterTurret();
        limelight = new Limelight();

        shuffleboard = new HashMap<String, NetworkTableEntry>();
        ShuffleboardTab tab = Shuffleboard.getTab("Design Testing");
        ShuffleboardLayout layout = tab.getLayout("Controls", BuiltInLayouts.kList);
        shuffleboard.put("upperFlywheelSpeed", layout.add("Upper Flywheel RPM", 0)
            .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 6000)).getEntry());
        shuffleboard.put("lowerFlywheelSpeed", layout.add("Lower Flywheel RPM", 0)
            .withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 6000)).getEntry());
        shuffleboard.put("preshooterSpeed", layout.add("Preshooter RPM", 0).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 6000)).getEntry());
        shuffleboard.put("indexerSpeed", layout.add("indexer RPM", 0).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 1)).getEntry());

        tab.add("Toggle Flywheel and Preshooter", new ToggleShooterAndPreshooter(flywheel, indexer));
        tab.add("Toggle Indexer", new ToggleIndexerIntake(indexer));
        tab.add("Toggle System", new ToggleSystem(flywheel, indexer));

        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return new CommandBase() { };
    }


}
