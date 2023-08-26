package frc.robot.subsystems.cases;

import frc.robot.RobotContainer;

/**
 * Данный класс предназначен для выполнения остановки выполнения комманд роботом
 */
public class End implements IState
{
    @Override
    public boolean execute() {
        RobotContainer.train.setAxisSpeed(0,0,0,false);

        RobotContainer.train.setGreen(true);
        RobotContainer.train.setRed(false);
        
        return false;
    }
}