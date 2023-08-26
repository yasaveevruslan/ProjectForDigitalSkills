package frc.robot.subsystems;

import frc.robot.subsystems.cases.IState;
import frc.robot.subsystems.cases_oms.CommandOMS;
import frc.robot.subsystems.cases_oms.ElevatorForLogic;
import frc.robot.subsystems.cases_oms.Grab;
import frc.robot.subsystems.cases_oms.SetDegrees;
import frc.robot.subsystems.cases_oms.StatefulOMS;

/**
 * Данный класс предназначен для прописания комманд OMS, также для запуска комманд для выполнения роботом
 */
public class StatesOMS
{

    public static final String WAREHOUSE = "warehouse";
    public static final String PALACE = "palace";
    public static final String ROBOT = "robot";    
    public static final String CAMERA = "camera";

    public static final String AUTONOMOUS = "autonomous";
    public static final String TELEOP = "teleop";

    public static final int SERVO_OPEN = 195;   
    public static final int SERVO_CLOSE_CUBE = 115;
    public static final int SERVO_CLOSE = 98;


    public static IState[][] statesOMS = new IState[][]
    {
        { // 0
            new StatefulOMS(CommandOMS.StartOMS)
        },
        { // 1
            new SetDegrees(StatesOMS.TELEOP, 1),
            new ElevatorForLogic(StatesOMS.PALACE, StatesOMS.AUTONOMOUS, 1),
            new Grab(SERVO_CLOSE),
            new StatefulOMS(CommandOMS.InitializationElevator),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new SetDegrees(StatesOMS.AUTONOMOUS, 0),
            new ElevatorForLogic(StatesOMS.ROBOT, StatesOMS.AUTONOMOUS, 0),
            new Grab(SERVO_OPEN),
            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 0),
            new StatefulOMS(CommandOMS.AddCubeInRobot),

            new StatefulOMS(CommandOMS.EndOMS),


        },
        { // 2

            new StatefulOMS(CommandOMS.SubstractCubeInRobot),
            new SetDegrees(StatesOMS.AUTONOMOUS, 0),
            new ElevatorForLogic(StatesOMS.ROBOT, StatesOMS.AUTONOMOUS, 0),
            new Grab(SERVO_CLOSE),

            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 1),
            new ElevatorForLogic(StatesOMS.PALACE, StatesOMS.AUTONOMOUS, 1),
            new Grab(SERVO_OPEN),

            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 0),

            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new StatefulOMS(CommandOMS.EndOMS),

        },

        { // 3
            new SetDegrees(StatesOMS.TELEOP, 5),
            new ElevatorForLogic(StatesOMS.WAREHOUSE, StatesOMS.AUTONOMOUS, 1),
            new Grab(SERVO_OPEN),   
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),

        },
        { // 4
            new Grab(SERVO_CLOSE_CUBE),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),


        },
        { // 5
            new Grab(SERVO_CLOSE),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),

        },
        { // 6
            new StatefulOMS(CommandOMS.InitializationElevator),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new SetDegrees(StatesOMS.AUTONOMOUS, 0),
            new ElevatorForLogic(StatesOMS.ROBOT, StatesOMS.AUTONOMOUS, 0),
            new Grab(SERVO_OPEN),
            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 0),
            new StatefulOMS(CommandOMS.AddCubeInRobot),

            new StatefulOMS(CommandOMS.EndOMS),


        },
        { // 7
            new StatefulOMS(CommandOMS.SubstractCubeInRobot),
            new SetDegrees(StatesOMS.AUTONOMOUS, 0),
            new ElevatorForLogic(StatesOMS.ROBOT, StatesOMS.AUTONOMOUS, 0),
            new Grab(SERVO_CLOSE),

            new StatefulOMS(CommandOMS.InitializationElevator),

            new SetDegrees(StatesOMS.TELEOP, 1),
            new Grab(SERVO_OPEN),

            new SetDegrees(StatesOMS.TELEOP, 0),

            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),

        },
        { // 8
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new StatefulOMS(CommandOMS.SubstractCubeInRobot),
            new SetDegrees(StatesOMS.AUTONOMOUS, 0),
            new ElevatorForLogic(StatesOMS.ROBOT, StatesOMS.AUTONOMOUS, 0),
            new Grab(SERVO_CLOSE),

            new StatefulOMS(CommandOMS.InitializationElevator),
            new StatefulOMS(CommandOMS.EndOMS),


        },
        { // 9
            new SetDegrees(StatesOMS.TELEOP, 1),
            new ElevatorForLogic(StatesOMS.PALACE, StatesOMS.AUTONOMOUS, 1),
            new Grab(SERVO_OPEN),

            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 0),

            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new StatefulOMS(CommandOMS.EndOMS),

        },
        { //10
            new SetDegrees(StatesOMS.TELEOP, 1),
            new ElevatorForLogic(StatesOMS.CAMERA, StatesOMS.TELEOP, 1),
            new Grab(SERVO_OPEN),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),

        },
        { //11
            new SetDegrees(StatesOMS.TELEOP, 1),
            new ElevatorForLogic(StatesOMS.CAMERA, StatesOMS.TELEOP, 2),
            // new Grab(SERVO_OPEN),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),

        },
        { //12
            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 0),

            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new StatefulOMS(CommandOMS.EndOMS),

        },
        { //13
            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 1),
            new Grab(SERVO_OPEN),

            new SetDegrees(StatesOMS.TELEOP, 0),

            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),

        },
        { //14
            new ElevatorForLogic(StatesOMS.PALACE, StatesOMS.AUTONOMOUS, 1),
            new Grab(SERVO_CLOSE),
            new StatefulOMS(CommandOMS.InitializationElevator),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new SetDegrees(StatesOMS.AUTONOMOUS, 0),
            new ElevatorForLogic(StatesOMS.ROBOT, StatesOMS.AUTONOMOUS, 0),
            new Grab(SERVO_OPEN),
            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 0),
            new StatefulOMS(CommandOMS.AddCubeInRobot),

            new StatefulOMS(CommandOMS.EndOMS),

        },
        { //15
            new ElevatorForLogic(StatesOMS.PALACE, StatesOMS.TELEOP, 3),
            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),
            new StatefulOMS(CommandOMS.EndOMS),

        },
        { // 16
            new StatefulOMS(CommandOMS.InitializationElevator),

            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new StatefulOMS(CommandOMS.EndOMS),

        },
        { // 16
            new StatefulOMS(CommandOMS.InitializationElevator),
            new SetDegrees(StatesOMS.TELEOP, 0),

            new StatefulOMS(CommandOMS.ActiveDrive),
            new StatefulOMS(CommandOMS.ResetActive),

            new StatefulOMS(CommandOMS.EndOMS),

        },


        ///Не используется
        // { // 16
        //     new StatefulOMS(CommandOMS.SubstractCubeInRobot),
        //     new SetDegrees(StatesOMS.AUTONOMOUS, 0),
        //     new ElevatorForLogic(StatesOMS.ROBOT, StatesOMS.AUTONOMOUS, 0),
        //     new Grab(SERVO_CLOSE),

        //     new StatefulOMS(CommandOMS.InitializationElevator),
        //     new SetDegrees(StatesOMS.TELEOP, 1),
        //     new ElevatorForLogic(StatesOMS.PALACE, StatesOMS.TELEOP, 2),
        //     new Grab(SERVO_OPEN),
            
        //     new StatefulOMS(CommandOMS.InitializationElevator),
        //     new SetDegrees(StatesOMS.TELEOP, 0),
        //     new ActiveDrive(),
        //     new ResetActive(),
        //     new StatefulOMS(CommandOMS.EndOMS),

        // },
        // { // 17
        //     new SetDegrees(StatesOMS.TELEOP, 1),
        //     new ElevatorForLogic(StatesOMS.WAREHOUSE, StatesOMS.TELEOP, 1),
        //     new Grab(SERVO_OPEN),   
        //     new ActiveDrive(),
        //     new ResetActive(),
        //     new StatefulOMS(CommandOMS.EndOMS),

        // },
    

    };
}