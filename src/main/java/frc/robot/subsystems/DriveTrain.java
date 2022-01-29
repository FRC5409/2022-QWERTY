package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase{


    private WPI_TalonSRX mot_left_front;
    private WPI_TalonSRX mot_left_rear;
    private WPI_TalonSRX mot_right_front;
    private WPI_TalonSRX mot_right_rear;

    private DifferentialDrive m_drive;

    
    public DriveTrain(){
        mot_left_front = new WPI_TalonSRX(Constants.kDriveTrain.left_front_id);
        mot_left_rear = new WPI_TalonSRX(Constants.kDriveTrain.left_rear_id);

        mot_right_front = new WPI_TalonSRX(Constants.kDriveTrain.right_front_id);
        mot_right_rear = new WPI_TalonSRX(Constants.kDriveTrain.right_rear_id);

        mot_left_front.setInverted(true);
        mot_left_rear.setInverted(true);

        mot_left_rear.follow(mot_left_front);
        mot_right_rear.follow(mot_right_front);

        m_drive = new DifferentialDrive(mot_left_front, mot_right_front);

    }

    /**
     * @param acceleration the robot's forward speed 
     * 
     * @param deceleration the robot's backward speed
     * 
     * @param turn         the robot's angular speed about the z axis
     * 
     */
    public void aadilDrive(final double acceleration, final double deceleration, final double turn){
        double accelerate = acceleration - deceleration;

        m_drive.arcadeDrive(accelerate, turn, true);
    }
    
}
