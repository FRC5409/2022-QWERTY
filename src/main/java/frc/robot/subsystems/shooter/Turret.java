package frc.robot.subsystems.shooter;

import java.util.HashMap;
import java.util.function.DoubleBinaryOperator;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase{


    private WPI_TalonFX mot_main;

    private boolean enabled;


    private HashMap<String, NetworkTableEntry> shuffleboardFields;
    private double target;

    /**
     * Constructor for the turret. 
     */
    public Turret(){
        mot_main = new WPI_TalonFX(Constants.kTurret.MAIN_MOTOR_ID);
        mot_main.configFactoryDefault();
        mot_main.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);

        enabled = false;


        shuffleboardFields = new HashMap<String, NetworkTableEntry>();
        
        ShuffleboardTab tab = Shuffleboard.getTab("Turret");
        ShuffleboardLayout shooterControls = tab.getLayout("Turret Controls: ", BuiltInLayouts.kList);


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
    public void enable(){
        enabled = true;
        setRotationTarget(target);
    }

    /**
     * Method for disabling the turret subsystem.
     */
    public void disable(){
        enabled = false;
    }

    /**
     * Method for setting the rotation target of the turret.
     * 
     * @param newTarget Angle to turn to in degrees, can be negative for turning left, positive for turning right.
     */
    public void setRotationTarget(double newTarget){
        //TODO add safetys
        mot_main.setSelectedSensorPosition(0);

        target = newTarget;

        double targetRadians = target*Math.PI / 180;
        double arcLengthTurret = targetRadians*Constants.Turret.TURRET_RADIUS;
        double positionForMotor = (arcLengthTurret / (2*Math.PI*Constants.Turret.GEAR_RADIUS)) * Constants.Falcon500.unitsPerRotation;
        

        mot_main.set(TalonFXControlMode.Position, positionForMotor);

    }


    /**
     * Method for getting the rotation target of the turret.
     */
    public double getRotationTarget(){
        return target;
    }

    /**
     * Method for getting the current rotation.
     * 
     * @return Angle in degrees.
     */
    public double getRotation(){
        double arcLengthTurret = mot_main.getSelectedSensorPosition() / Constants.Falcon500.unitsPerRotation * (2*Math.PI*Constants.Turret.GEAR_RADIUS);
        double degrees = arcLengthTurret/Constants.Turret.TURRET_RADIUS * 180 / Math.PI;
        return degrees;
    }

    /**
     * Method for getting if the turret is aligned with its target.
     * @return True if its aligned, false if not. 
     */
    public boolean isAligned(){

        return false;
    }


    
}
