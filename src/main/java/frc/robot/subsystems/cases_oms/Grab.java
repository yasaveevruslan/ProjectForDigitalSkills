package frc.robot.subsystems.cases_oms;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StateMachineOMS;
import frc.robot.subsystems.Training;
import frc.robot.subsystems.cases.IState;

/**
 *  Класс является структурой комманд связанных с поворот OMS
 *  Может выполнять различные состояния поворота OMS
 */
public class Grab implements IState
{

    private int position;

    public Training train = RobotContainer.train;


    
    public Grab(int position)
    {
        this.position = position;
    }



    @Override
    public boolean execute() 
    {
        return methodGrab(this.position);
    }

    /**
     *  Метод для выполнения установки угла на servo хвата
     */

    private boolean methodGrab(int position)
    {
        if (position != -1)
        {
            train.angleGrabServo = position;
        }

        return (float)Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > 0.5f;
    }
    
}