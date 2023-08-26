package frc.robot.subsystems.cases;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.StateMachineOMS;

/**
 * Данный класс предназначен для вызова параллельного лифта
 */
public class OMS implements IState{

    private int indexIn;

    public OMS(int indexIn){
        this.indexIn = indexIn;

    }
    
    @Override
    public boolean execute() {
        RobotContainer.train.setAxisSpeed(0, 0, 0, false);

        StateMachineOMS.startOMS = true;

        if (StateMachine.isFirst){
            StateMachineOMS.changeArray = this.indexIn;
            StateMachineOMS.changeOMS = false;
            StateMachine.isFirst = false;
        }
        

        
        return StateMachineOMS.endOMS && Timer.getFPGATimestamp() - StateMachine.StartTime > 0.2f;
    }


    
}