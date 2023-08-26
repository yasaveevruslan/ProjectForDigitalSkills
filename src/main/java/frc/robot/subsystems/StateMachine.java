package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

/**
 * Данный класс предназначен для управления состовляющими робота и обновления комманд маршрутов и логики
 * Также для реализации обновления комманд для параллельной OMS
 */
public class StateMachine extends CommandBase {

    public static int CurrentArray = 0;
    public static int CurrentIndex = 0;

    private static final String warehouseLogic = "warehouse";
    private static final String retLogic = "ret";
    private static final String deliveryLogic = "delivery";

    /**
     * Чтобы запустить выполнения автономности выполнения моделей доставки, вывозки и работы со складом
     * В этой переменной необходимо поставить переменную, которая отвечает за логику
     */
    public static String commandLogic = deliveryLogic;

    public static float StartTime = 0;
    public static boolean isFirst = true;

    public static int numberPalace = 0;
    public static int indexElementLogic = 0;

    public static int startOMS = 0;

    /**
     * Отвечает за общение со состовляющими робота
     */

    public StateMachine() {
        addRequirements(RobotContainer.train);
    }

    /**
     * Необходимые комманды при запуске робота
     */

    @Override
    public void initialize() {
        RobotContainer.train.resetCoordinates(0, 0);
        RobotContainer.train.resetYaw(0);
    }

    /**
     * Необходимые комманды, которые должны выполняться во время работы программы
     */

    @Override
    public void execute() {
        RobotContainer.train.CalculateOdometry();
        Update();
        StateMachineOMS.UpdateOMS();
    }

    
    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * Метод для выполнения ваших автономных комманд, которые вы прописали в классе States
     */

    public static void Update() {

        if (States.logStates[CurrentArray][CurrentIndex].execute()) {
            StartTime = (float) Timer.getFPGATimestamp();
            isFirst = true;
            CurrentIndex++;
        }

        SmartDashboard.putNumber("array", CurrentArray);
        SmartDashboard.putNumber("index", CurrentIndex);
        SmartDashboard.putNumber("startOMS", startOMS);

    }

}