package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.SparkMaxPIDController;

public final class MotorUtils {
    public static final WPI_TalonFX setGains(WPI_TalonFX motor, int slotIdx, Gains gains) {
        motor.config_kP(slotIdx, gains.P);
        motor.config_kI(slotIdx, gains.I);
        motor.config_kD(slotIdx, gains.D);
        motor.config_kF(slotIdx, gains.F);

        return motor;
    }

    public static final SparkMaxPIDController setGains(SparkMaxPIDController controller, Gains gains) {
        controller.setP(gains.P);
        controller.setI(gains.I);
        controller.setD(gains.D);
        controller.setFF(gains.F);

        return controller;
    }
}
