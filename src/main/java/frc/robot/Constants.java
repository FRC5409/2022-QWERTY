// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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

    public final class ShooterFlywheel {
        public static final int UPPER_MOTOR_ID = 10;

        public static final int LOWER_MOTOR_ID = 7;

        //in RPM
        public static final int SHOOTER_TOLERANCE = 50;


        public static final int rpmTolerance = 1;

        public static final double UPPER_P = 0.29873;
        public static final double UPPER_I = 0;
        public static final double UPPER_D = 0;
        public static final double UPPER_FF = 0.044265063;

        public static final double LOWER_P = 0.11;
        public static final double LOWER_I = 0;
        public static final double LOWER_D = 0;
        public static final double LOWER_FF = 0.047293811;



        
    }


    public final class Turret {
        //Ratio including gearbox 
        //126 : 1
        public static final double GEAR_RATIO = 126;

        public static final int LIMIT_SWITCH_CHANNEL =  1;


        // Height in meters
        public static final double ROBOT_HEIGHT = 4;
        public static final double FIXED_ANGLE = 45;
        public static final int MAIN_MOTOR_ID = 12;
        public static final double UPPER_LIMIT = 20;
        public static final double LOWER_LIMIT = -10;

        public static final double TARGET_THRESHOLD = 0.01;


        public static final double P =  0.35;
        public static final double I =  0;
        public static final double D =  1.852;
        public static final double F =  0.0;


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

}
