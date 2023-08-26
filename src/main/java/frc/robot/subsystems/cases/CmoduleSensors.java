package frc.robot.subsystems.cases;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StateMachine;

/**
 * Данный класс специально создан для выполнения двух заданий из модуля B
 */
public class CmoduleSensors implements IState
{

    private String commandSensors;

    public CmoduleSensors(String commandSensors){
        this.commandSensors = commandSensors;
    }

    @Override
    public boolean execute() {
        boolean end = false;

        if(commandSensors.equals("sonic")){
            if(RobotContainer.train.getFrontSonic() < 18 || RobotContainer.train.getRightSonic() < 18)
            {
                end = true;
            }
            else
            {
                end = false;
            }
        }else{
            if(RobotContainer.train.getBackSharp() < 18 || RobotContainer.train.getFrontSharp() < 18)
            {
                end = true;
            }
            else
            {
                end = false;
            }
        }

        if(end){
            RobotContainer.train.setGreen(true);
        }else{
            RobotContainer.train.setGreen(false);
        }
        
        return end && RobotContainer.train.getButtonStart() && Timer.getFPGATimestamp() - StateMachine.StartTime > 0.5f;
    }
}
