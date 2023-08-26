package frc.robot.subsystems.cases;

import frc.robot.RobotContainer;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.Training;

/**
 * Данный класс предназначен для выполнения ожидания запуска робота и выполнения инциализаций различных систем
 */
public class Start implements IState
{
    public Training train = RobotContainer.train;

    @Override
    public boolean execute() {
        if (StateMachine.isFirst){
            RobotContainer.train.resetYaw(0);
        }

        RobotContainer.train.resetCoordinates(0, 0);
        RobotContainer.train.resetZeroYaw(0);
        RobotContainer.train.setAxisSpeed(0, 0, 0, false);
        
        if(RobotContainer.train.getButtonStart()) 
        {
            RobotContainer.train.setGreen(false);
            RobotContainer.train.setRed(true);
            StateMachine.startOMS++;
            return true;
        }else
        {
            RobotContainer.train.setRed(true);
            RobotContainer.train.setGreen(true);
            return false;
        }
    }
    
}