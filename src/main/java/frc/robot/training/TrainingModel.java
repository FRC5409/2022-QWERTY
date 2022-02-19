package frc.robot.training;

import frc.robot.Constants;

public class TrainingModel {
    public final double kA;
    public final double kB;
    public final double kC;
    public final double kD;
    
    public TrainingModel(double kA, double kB, double kC, double kD) {
        this.kA = kA;
        this.kB = kB;
        this.kC = kC;
        this.kD = kD;
    }

    public double calculate(double x) {
        x = Constants.Shooter.DISTANCE_RANGE.normalize(x);
        return Constants.Shooter.TARGET_RANGE.scale(kA*x*x*x + kB*x*x + kC*x + kD);
    }
}
