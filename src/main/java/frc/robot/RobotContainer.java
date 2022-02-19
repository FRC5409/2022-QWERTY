// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.base.Joystick;
import frc.robot.base.Joystick.ButtonType;

import frc.robot.commands.*;
import frc.robot.commands.shooter.*;
import frc.robot.commands.training.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.shooter.*;
import frc.robot.training.*;
import frc.robot.training.protocol.*;
import frc.robot.training.protocol.generic.*;
import frc.robot.util.*;

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
    private final ShooterFlywheel      flywheel;
    private final Colour               colour;
    private final IndexerProto         indexer;
    private final Limelight            limelight;
    private final ShooterTurret        turret;


    // Define main joystick
    private final Joystick             joystick0;
    private final Joystick             joystick1;

    HashMap<String, NetworkTableEntry> shuffleboard;

    private NetworkClient              trainerClient;
    private TrainerContext             trainerContext;
    private TrainerDashboard           trainerDashboard;



    /**
     * The container for the robot. Contains subsystems, IO devices, and commands.
     */
    public RobotContainer() {
        joystick0 = new Joystick(0);
        joystick1 = new Joystick(1);

        colour = new Colour();
        flywheel = new ShooterFlywheel();
        indexer = new IndexerProto();
        turret = new ShooterTurret();
        limelight = new Limelight();

        shuffleboard = new HashMap<String, NetworkTableEntry>();

        ShuffleboardTab tab = Shuffleboard.getTab("Design Testing");
        ShuffleboardLayout layout = tab.getLayout("Controls", BuiltInLayouts.kList);

        shuffleboard.put("upperFlywheelSpeed", layout.add("Upper Flywheel RPM", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 6000))
            .getEntry());

        shuffleboard.put("lowerFlywheelSpeed", layout.add("Lower Flywheel RPM", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 6000))
            .getEntry());

        shuffleboard.put("preshooterSpeed", layout.add("Preshooter RPM", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 6000))
            .getEntry());

        shuffleboard.put("indexerSpeed", layout.add("indexer RPM", 0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 1))
            .getEntry());

        try {
            configureTraining();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        configureButtonBindings();
    }

    private void configureTraining() throws IOException {
		SendableContext context = new SendableContext();
            context.registerSendable(ValueSendable.class);
            context.registerSendable(KeyValueSendable.class);
            context.registerSendable(StringSendable.class);
			  
		NetworkSocket socket = NetworkSocket.create(Constants.Training.TRAINER_HOSTNAME);

		trainerClient = new NetworkClient(socket, context);
		trainerContext = new TrainerContext(
			new Setpoint(1000, new Range(0, 6000)),
            new ShooterModel(0.0, 0.0, 0.0, 0.0, Constants.Shooter.DISTANCE_RANGE, Constants.Shooter.SPEED_RANGE)
		);
		trainerDashboard = new TrainerDashboard(trainerContext);
    }

    private void configureButtonBindings() {
        joystick0.getButton(ButtonType.kA)
            .whileHeld(new TrainerRunShooter(limelight, turret, flywheel, indexer, trainerDashboard, trainerContext));

        joystick0.getButton(ButtonType.kLeftBumper)
            .whileHeld(new SpinIndexer(indexer, -1));

        joystick0.getButton(ButtonType.kRightBumper)
            .whileHeld(new SpinIndexer(indexer, 1));

        joystick1.getButton(ButtonType.kX)
            .whenPressed(new BranchTargetSetpoint(trainerDashboard, trainerContext, BranchType.BRANCH_LEFT));
		
        joystick1.getButton(ButtonType.kB)
            .whenPressed(new BranchTargetSetpoint(trainerDashboard, trainerContext, BranchType.BRANCH_RIGHT));

        joystick1.getButton(ButtonType.kRightBumper)
		    .whenPressed(new BranchTargetSetpoint(trainerDashboard, trainerContext, BranchType.BRANCH_CENTER));

        joystick1.getButton(ButtonType.kLeftBumper)
		    .whenPressed(new RequestModelUpdate(trainerDashboard, trainerClient, trainerContext));

        joystick1.getButton(ButtonType.kY)
            .whenPressed(new FlipTargetSetpoint(trainerDashboard, trainerContext));
        
        joystick1.getButton(ButtonType.kStart)
            .whenPressed(new SubmitSetpointData(trainerDashboard, trainerClient, trainerContext));

        joystick1.getButton(ButtonType.kLeftStick)
            .whenPressed(new ResetTargetSetpoint(trainerDashboard, trainerContext));

        joystick1.getButton(ButtonType.kA)
            .whileHeld(new TrainerLookShooter(limelight, turret, trainerDashboard, trainerContext))
            .whenReleased(new RotateTurret(turret, 0));

        joystick1.getButton(ButtonType.kStart)
            .whenPressed(new UndoTargetSetpoint(trainerDashboard, trainerContext));    

        joystick1.getButton(ButtonType.kStart)
            .whenPressed(new UndoTargetSetpoint(trainerDashboard, trainerContext));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new CommandBase() { };
    }
}
