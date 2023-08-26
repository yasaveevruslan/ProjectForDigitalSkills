package frc.robot.subsystems.cases;

import frc.robot.RobotContainer;
import frc.robot.logic.InitLogic;

/**
 * Данный класс предназначен для запуска инциализации логики
 * Класс необходимо вызывать в самом первом массиве
 */
public class InitializeLogic implements IState 
{

    private InitLogic log = RobotContainer.initlogic;

    @Override
    public boolean execute() 
    {
        
        log.initCmodule();
        RobotContainer.train.setAxisSpeed(0, 0, 0, false);

        return true;
    }

}