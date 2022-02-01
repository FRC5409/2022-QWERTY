// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public final class kDriveTrain{
        public static final int left_front_id = 51;
        public static final int left_rear_id = 52;
        public static final int right_front_id = 53;
        public static final int right_rear_id = 54;

    }

    public final class ShooterFlywheel{
        public static final int UPPER_MOTOR_ID = 10;
        public static final int LOWER_MOTOR_ID= 7;
        
        public static final int rpmTolerance = 1;


        public static final double UPPER_P = 0.07;
        public static final double UPPER_I = 0;
        public static final double UPPER_D = 0;
        public static final double UPPER_FF = 0.048;

        public static final double LOWER_P = 0.07;
        public static final double LOWER_I = 0;
        public static final double LOWER_D = 0;
        public static final double LOWER_FF = 0.048;
    }

    public final class Turret{
        public static final double TURRET_RADIUS = 0;
        public static final double GEAR_RADIUS = 0;

        //Height in meters
        public static final double ROBOT_HEIGHT = 4;
        public static final double FIXED_ANGLE = 45;

    }

    public final class Falcon500{
        public static final double unitsPerRotation = 2048;
    }

    public final class kTurret{
        public static final int MAIN_MOTOR_ID = 0;

    }

    public final class kColour{
        public static final int proximityThreshold = 1000;
    }
  
    public final class kIndexer{
        public static final int currentLimit = 20; 
        public static final int kIndexBeltMotor = 31;
        public static final int kIndexShooterMotor = 30;
    }

}
