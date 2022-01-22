package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase{


    private WPI_TalonFX mot_main;

    private boolean enabled;

    private double target;

    /**
     * Constructor for the turret. 
     */
    public Turret(){
        mot_main = new WPI_TalonFX(Constants.kTurret.MAIN_MOTOR_ID);
        mot_main.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void periodic() {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void simulationPeriodic() {

    }

    /**
     * Method for enabling the turret subsystem.
     */
    public void enable(){}

    /**
     * Method for disabling the turret subsystem.
     */
    public void disable(){}

    /**
     * Method for setting the rotation target of the turret.
     */
    public void setRotationTarget(){}


    /**
     * Method for getting the rotation target of the turret.
     */
    public void getRotationTarget(){}

    /**
     * Method for getting the current rotat
     */
    public void getRotation(){}

    /**
     * Method for getting if the turret is aligned with its target.
     * @return True if its aligned, false if not. 
     */
    public boolean isAligned(){

        return false;
    }


    
}
