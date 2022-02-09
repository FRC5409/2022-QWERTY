package frc.robot.subsystems.shooter;

import java.util.HashMap;
import java.util.function.DoubleBinaryOperator;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase{


    //private WPI_TalonFX mot_main;
    private CANSparkMax mot_main; 
    private RelativeEncoder enc_main;
    private SparkMaxPIDController controller_main;


    private boolean enabled;


    private HashMap<String, NetworkTableEntry> shuffleboardFields;
    private double target;

    /**
     * Constructor for the turret. 
     */
    public Turret(){
        mot_main = new CANSparkMax(Constants.kTurret.MAIN_MOTOR_ID, MotorType.kBrushless);
        //mot_main = new WPI_TalonFX(Constants.kTurret.MAIN_MOTOR_ID);
        mot_main.restoreFactoryDefaults();
        //mot_main.configFactoryDefault();
        //mot_main.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);

        enc_main = mot_main.getEncoder();
        enc_main.setPosition(0);

        controller_main = mot_main.getPIDController();
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

        target = newTarget;

        double motorRotationPerDegree = Constants.Turret.GEAR_RATIO / 360;
        

        controller_main.setReference(motorRotationPerDegree*target, CANSparkMax.ControlType.kPosition);
        //mot_main.set(TalonFXControlMode.Position, positionForMotor);

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
        double motorRotationPerDegree = Constants.Turret.GEAR_RATIO / 360;
        return enc_main.getPosition()/motorRotationPerDegree;
    }



    /**
     * Method for getting if the turret is aligned with its target.
     * @return True if its aligned, false if not. 
     */
    public boolean isAligned(){

        return false;
    }


    
}
