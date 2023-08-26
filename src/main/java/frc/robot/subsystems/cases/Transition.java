package frc.robot.subsystems.cases;

import frc.robot.RobotContainer;
import frc.robot.logic.InitLogic;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.StateMachineOMS;

/**
 * Данный класс предназначен для перехода по массивам комманд
 */
public class Transition implements IState
{

    private InitLogic log = RobotContainer.initlogic;
    
    @Override
    public boolean execute() 
    {
        StateMachine.CurrentArray = log.indexMas.get(StateMachine.indexElementLogic)[0]+1;
        StateMachineOMS.autonomousPositionLift = log.indexMas.get(StateMachine.indexElementLogic)[1];
        StateMachine.CurrentIndex=-1;
        // StateMachine.CurrentArray++;
        StateMachine.indexElementLogic++;
        return true;
    }

}