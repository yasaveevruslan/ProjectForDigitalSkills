package frc.robot.subsystems.cases;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StateMachine;

/**
 * Данный класс предназначен для выполнения сброса систем
 * Выбор систем для сброса, выполняется взависимости от вашей комманды
 */
public class Reset implements IState
{

    private float x, y, z;
    private String element;

    public Reset(String element, float x, float y, float z)
    {
        this.element = element;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean execute() {
        switch(this.element)
        {
            case "gyro":
            RobotContainer.train.resetYaw(this.z);
            break;

            case "all":
            RobotContainer.train.resetCoordinates(this.x, this.y);
            RobotContainer.train.resetYaw(this.z);
            break;
        
        }
        RobotContainer.train.setAxisSpeed(0, 0, 0, false);
        return Timer.getFPGATimestamp() - StateMachine.StartTime > 0.28f;
    }

}