package frc.robot.subsystems.cases_oms;

import java.util.Arrays;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.StateMachineOMS;
import frc.robot.subsystems.StatesOMS;
import frc.robot.subsystems.Training;
import frc.robot.subsystems.cases.IState;

/**
 *  Класс является структурой комманд связанных с позицией OMS
 *  Может выполнять различные состояния позиций OMS
 */

public class ElevatorForLogic implements IState
{

    public Training train = RobotContainer.train;

    private int[] shelfPos = {0, 0, -348, -688};
    private int[] palacePos = {0, -670, -520, -378, -239};
    private int[] robotPosLift = {0, -516, -386, -255, -178};
    private int[] cameraPos = {0, -700, -665};

    private String name, command;
    private int position;

    private int[] mas;

    private boolean endLift = false;



    public ElevatorForLogic(String name, String command, int position)
    {
        this.name = name;
        this.command = command;
        this.position = position;
    }
    
    /**
     *  Метод для выбора типа комманд и уставнока позиций
     */

    @Override
    public boolean execute()
    {


        switch (this.name)
        {
            case StatesOMS.WAREHOUSE:
            this.mas = Arrays.copyOf(this.shelfPos, this.shelfPos.length);
            this.endLift = this.methodElevator(this.command, this.position, this.mas);
            break;

            case StatesOMS.PALACE:
            this.mas = Arrays.copyOf(this.palacePos, this.palacePos.length);
            this.endLift = this.methodElevator(this.command, this.position, this.mas);
            break;

            case StatesOMS.CAMERA:
            this.mas = Arrays.copyOf(this.cameraPos, this.palacePos.length);
            this.endLift = this.methodElevator(this.command, this.position, this.mas);
            break;

            case StatesOMS.ROBOT:
            this.mas = Arrays.copyOf(this.robotPosLift, this.robotPosLift.length);
            this.endLift = this.methodElevatorRobot(this.command, this.position, this.mas);
            break;

        }
        return this.endLift;
    }

    /**
     *  Для выполнения комманд и установки позиции вне робота
     */

    private float pos = 0;
    private boolean methodElevator(String command, int position, int[] mas)
    {
        train.initializeLift = false;

        if (command.equals(StatesOMS.AUTONOMOUS))
        {
            this.pos = mas[StateMachineOMS.autonomousPositionLift];
        }else{
            this.pos = mas[position];
        }

        if (position != -1)
        {
            train.positionLift = this.pos;
        }
        return train.liftPosReached && (float)Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > 0.2f;
    }


    /**
     *  Для выполнения комманд и установки позиции в роботе
     */
    
    private float posRobot = 0;
    private boolean methodElevatorRobot(String command, int position, int[] mas){
        train.initializeLift = false;

        if (command.equals(StatesOMS.AUTONOMOUS)){
            this.posRobot = this.robotPosLift[(StateMachineOMS.cubeInRobot / 3) + 1];
        }else{
            this.posRobot = this.robotPosLift[position];
        }

        if (position != -1){
            train.positionLift = this.posRobot;
        }
        return train.liftPosReached && (float)Timer.getFPGATimestamp() - StateMachineOMS.TimeOMS > 0.2f;
    }
}