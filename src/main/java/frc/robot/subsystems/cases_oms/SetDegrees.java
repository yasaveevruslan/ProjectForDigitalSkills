package frc.robot.subsystems.cases_oms;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.StateMachineOMS;
import frc.robot.subsystems.StatesOMS;
import frc.robot.subsystems.Training;
import frc.robot.subsystems.cases.IState;

/**
 *  Класс является структурой комманд связанных с поворот OMS
 *  Может выполнять различные состояния поворота OMS
 */
public class SetDegrees implements IState
{
    public Training train = RobotContainer.train;

    private int[] degreesPosition = {90, -5, 130, 182, 231, -5};

    private String name;

    private float degreesFirst, time, substract;

    private int position;
    private int angle = 0;
    private float speedServoMotors = 0;


    public SetDegrees(String name, int position)
    {
        this.name = name;
        this.position = position;
    }



    @Override
    public boolean execute() 
    {
        if (StateMachineOMS.isFirstOMS)
        {
            this.degreesFirst = train.getDegreesServo();
        }
        
        return methodDegrees(this.name, this.position);
    }


    
    
    /**
     *  Логика поворота
     */
    private boolean methodDegrees(String name, int position)
    {

        if (StatesOMS.AUTONOMOUS.equals(name))
        {
            this.angle = this.degreesPosition[(StateMachineOMS.cubeInRobot % 3) + 2];
        }
        else
        {
            this.angle = this.degreesPosition[position];
        }

        if (StateMachineOMS.isFirstOMS)
        {
            this.time = 0;
            this.substract = this.degreesFirst - this.angle;
    
            if (Function.InRangeBool(this.substract, -100, 100))
            {
                this.time = 0.5f;
            }
            else if (Function.InRangeBool(this.substract, -190, 190))
            {
                this.time = 1f;
            }
            else
            {
                this.time = 1.5f;
            }

            StateMachineOMS.isFirstOMS = false;
        }


        if (position != -1)
        {
            if (this.degreesFirst > this.angle)
            {

                float[][] functionDegrees = {{0, this.time}, {0, this.degreesFirst - this.angle}};
                float speed = Function.TransF(functionDegrees, (float)Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS);
                this.speedServoMotors = this.degreesFirst - speed;
            }
            else if (this.angle > this.degreesFirst)
            {
                float[][] functionDegrees = {{0, this.time}, {this.degreesFirst, this.angle}};
                float speed = Function.TransF(functionDegrees, (float)Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS);
                this.speedServoMotors = speed;
            }
            else
            {
                this.speedServoMotors = this.angle;
            }
        }
        
        SmartDashboard.putNumber("angleServo", train.angleDegreesServo);

        if ((float)Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > this.time + 0.2f)
        {
            train.angleDegreesServo = this.angle;
            return true;
        }
        else
        {
            train.angleDegreesServo = this.speedServoMotors;
            return false;
        }
        // return (float)Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > this.time + 0.2f;
    }
}