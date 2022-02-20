// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.util.Equation;
import frc.robot.util.Gains;
import frc.robot.util.Range;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public final class kDriveTrain {
        public static final int left_front_id = 51;
        public static final int left_rear_id = 52;
        public static final int right_front_id = 53;
        public static final int right_rear_id = 54;
    }

    public static final class ShooterFlywheel {
        public static final int UPPER_MOTOR_ID = 10;
        public static final int LOWER_MOTOR_ID = 7;
        public static final int FEEDER_MOTOR_ID = 30;


        //in RPM
        public static final int SHOOTER_TOLERANCE = 240;
        public static final int FEEDER_TOLERANCE = 100;
        public static final int rpmTolerance = 1;

        public static final Gains FEEDER_GAINS = new Gains(0.0001, 0.0, 0.0, 0.00017);
        public static final Gains UPPER_GAINS = new Gains(0.29873, 0, 0, 0.044265063);
        public static final Gains LOWER_GAINS = new Gains(0.011, 0, 0, 0.047293811);
    }


    public final static class Turret {
        //Ratio including gearbox 
        //126 : 1
        public static final double GEAR_RATIO          = 126;

        // Height in meters
        public static final double ROBOT_HEIGHT        = 4;
        public static final double FIXED_ANGLE         = 45;
        public static final int    MAIN_MOTOR_ID       = 12;
        public static final Range  LIMITS              = new Range(-10, 20);
        public static final double ALIGNMENT_THRESHOLD = 0.14;

        //TODO fill in pneumatics constants.
        public static final int HOOD_MODULE  =0;
        public static final int HOOD_FORWARD_CHANNEL = 0;
        public static final int HOOD_REVERSE_CHANNEL = 0;

        public static final int LIMIT_SWITCH_CHANNEL = 1;
    }

    public final class Falcon500 {
        public static final double unitsPerRotation = 2048;
    }

    public final class kColour {
        public static final int proximityThreshold = 100;
    }

    public final class kIndexer {
        public static final int currentLimit = 20;
        public static final int kIndexBeltMotor = 31;
        public static final int kIndexShooterMotor = 30;


        public static final double UPPER_P = 0.0001;
        public static final double UPPER_I = 0.0;
        public static final double UPPER_D = 0.0;
        public static final double UPPER_F = 0.00017;


        // in RPM
        public static final double PRESHOOTER_TOLERANCE = 50;

        public static final int TOF_Ent = 15; 
        public static final int TOF_Ball1 = 16; 
        public static final int TOF_Ext = 17; 

        public static final int sampleTime = 24; 

        public static final int rangeEnter_1 =  40; 
        public static final int rangeEnter_2 = 105; 

        public static final int rangeBall1_1 = 160; 
        public static final int rangeBall1_2 = 150; 

        public static final int rangeExit_1 = 140; 
        public static final int rangeExit_2 = 180;


    }

    public static final class Limelight {
        public static final String NETWORK_TABLE_NAME = "limelight";
    };

    public static final class Training {
        public static final Range  DISTANCE_RANGE = new Range(0.0, 20);
        public static final String TRAINER_HOSTNAME = "10.54.9.150";
    }
    
    public static final class Shooter {
        public static final double GEAR_RATIO          = 126;

        // Height in meters
        public static final double ROBOT_HEIGHT        = 4;
        public static final double FIXED_ANGLE         = 45;
        public static final Range  TARGET_RANGE        = new Range(-10, 20);
        public static final double ALIGNMENT_THRESHOLD = 0.14;
        public static final double TURRET_MAX_SPEED    = 0.42;

        // Range Configurations
        public static final Range ROTATION_RANGE = new Range(
            -28.571428571428573, 57.14285714285714
        );

        public static final Range SPEED_RANGE = new Range(
            0, 5500
        );

        public static final Range DISTANCE_RANGE = new Range(
            10, 33
        );

        
    // Curve fitting Constants
        public static final Equation DISTANCE_SPEED_CURVE = d -> {
            return d*0;
        };

        public static final double CALIBRATE_SPEED = 0.07;


    // Smooth Sweep Constants (experimental)
        public static final double SHOOTER_SWEEP_PERIOD = 3.6;

        public static final Equation SHOOTER_SWEEP_FUNCTION = new Equation() {
            private final double kA = (2.0 * Math.PI) / SHOOTER_SWEEP_PERIOD;
            private final double kB = 1.0 / ( 2.0 * ROTATION_RANGE.magnitude() );
            private final double kC = ROTATION_RANGE.min();
            
            @Override
            public double calculate(double x) {
                //return (Math.cos(kA * x) + 1.0) * kB + kC;
                return (Math.cos(2d*Math.PI*x/SHOOTER_SWEEP_PERIOD)+1d)/2d*ROTATION_RANGE.magnitude()+ROTATION_RANGE.min();
            }
        };

        public static final Equation SHOOTER_SWEEP_INVERSE = new Equation() {
            private final double kA = 2.0 * (1.0 / ROTATION_RANGE.magnitude());
            private final double kB = 1.0 / ( 2.0 * Math.PI );
            private final double kC = ROTATION_RANGE.min();
            
            @Override
            public double calculate(double x) {
                //return SHOOTER_SWEEP_PERIOD * Math.acos(kA * (x-kC) - 1.0) * kB;
                 return SHOOTER_SWEEP_PERIOD * Math.acos(2d * (x-ROTATION_RANGE.min()) / ROTATION_RANGE.magnitude() - 1d) / (Math.PI*2d);
            }
        };
        
        public static final double SHOOTER_MAX_SWEEEP = 2;
    
        public static final Gains TURRET_GAINS = new Gains(
            0.35d, 0.0, 1.852d, 0
        );

        public static final double ALIGNMENT_MAX_TIME = 2;

        public static final double PRE_SHOOTER_DISTANCE = 0;
    }
    
    public static final class Vision {
        public static final double TARGET_HEIGHT = 104d/12.0d;
        
        public static final double LIMELIGHT_HEIGHT = 39.5d/12.0d;

        public static final double LIMELIGHT_PITCH = 90 - 81.5;//13.4;//13.15

        public static final double ACQUISITION_DELAY = 0.35;

        public static final double ALIGNMENT_THRESHOLD = 1.5;

        public static final Equation DISTANCE_FUNCTION = new Equation() {
            private final double height = Math.abs(TARGET_HEIGHT - LIMELIGHT_HEIGHT);
            @Override
            public double calculate(double x) {
                return height / Math.tan(Math.toRadians(x + Constants.Vision.LIMELIGHT_PITCH));
            }
            
        };
    }
}
