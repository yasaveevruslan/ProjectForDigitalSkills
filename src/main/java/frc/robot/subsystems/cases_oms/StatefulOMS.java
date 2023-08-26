package frc.robot.subsystems.cases_oms;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StateMachineOMS;
import frc.robot.subsystems.cases.IState;

/**
 *  Класс является контроллером состояний работы OMS.
 *  Выполняет вызванное состояние и выполняет переход между состояниямию.
 */
public class StatefulOMS implements IState 
{
    private CommandOMS currentCommand;
    private final float TIME_END = 0.2f;

    public StatefulOMS(CommandOMS commandOMS) 
    {
        this.currentCommand = commandOMS;
    }

    @Override
    public boolean execute() 
    {
        boolean end = false;

        switch (currentCommand) 
        {
            case ActiveDrive:
                end = activeDrive();
                break;
            case AddCubeInRobot:
                end = updateAmountCubeInRobot(1);
                break;
            case SubstractCubeInRobot:
                end = updateAmountCubeInRobot(-1);
                break;
            case EndOMS:
                end = endOMS();
                break;
            case InitializationElevator:
                end = initializationElevator();
                break;
            case ResetActive:
                end = resetActive();
                break;
            case StartOMS:
                end = startOMS();
                break;
            default:
                end = true;
                break;
        }

        return end;
    }

    
    /**
     *  Включение движения базы робота
    */
    private boolean activeDrive() 
    {
        StateMachineOMS.endOMS = true;
        return Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > TIME_END;
    }


    /**
     *  Увеличивает/Уменьшает количество кубов в роботе
     *  Часть автоматического счёта при выполнии логики
     */
    private boolean updateAmountCubeInRobot(int amountCube)
    {
        if (StateMachineOMS.isFirstOMS) 
        {
            StateMachineOMS.cubeInRobot+=amountCube;
            StateMachineOMS.isFirstOMS = false;
        }
        return Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > TIME_END;
    }


    /**
     *  Окончание работы лифта
     */
    private boolean endOMS() 
    {
        if (StateMachineOMS.startOMS) 
        {
            StateMachineOMS.currentIndexOMS = -1;
            StateMachineOMS.currentArrayOMS = StateMachineOMS.changeArray;
            StateMachineOMS.endOMS = false;
            return true;
        }

        return false;
    }


    /**
     *  Инициализация лифта
     */
    private boolean initializationElevator() 
    {
        RobotContainer.train.initializeLift = true;
        return RobotContainer.train.getLimitSwitch() && Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > TIME_END;
    }


    /**
     *  Сбрасывание состояние лифта
     */
    private boolean resetActive() 
    {
        StateMachineOMS.endOMS = false;
        StateMachineOMS.startOMS = false;
        return Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > TIME_END;

    }


    /**
     *  Первоначальная инциализация состояния OMS
     *  Инциализация лифта, назначение нач. значенйи на servo
     */
    private boolean startOMS() 
    {
        if (RobotContainer.train.liftPosReached) 
        {
            RobotContainer.train.initializeLift = false;
            RobotContainer.train.angleDegreesServo = 90;
            RobotContainer.train.angleGrabServo = 200;
        }
        else 
        {
            RobotContainer.train.initializeLift = true;
            RobotContainer.train.angleGrabServo = 200;
        }

        StateMachineOMS.changeOMS = true;

        if (StateMachineOMS.startOMS) 
        {
            StateMachineOMS.currentArrayOMS = StateMachineOMS.changeArray;
            StateMachineOMS.currentIndexOMS = -1;
            return true;
        }

        return false;
    }
}
