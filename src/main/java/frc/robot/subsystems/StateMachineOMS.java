package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Данный класс предназначен для обновления комманд для параллельной OMS
 * Выполняет комманды, которые вы прописали в классе StatesOMS
 */
public class StateMachineOMS
{
    public static int currentArrayOMS;
    public static int currentIndexOMS;

    public static float TimeOMS = 0;
    public static boolean isFirstOMS = true;

    public static boolean startOMS = false;
    public static boolean endOMS = false;
    public static boolean changeOMS = false;
    public static int changeArray = 0;

    public static int cubeInRobot = 0;
    public static int autonomousPositionLift = 0;

    /**
     * Метод для выполнения ваших автономных комманд, которые вы прописали в классе StatesOMS
     */

    public static void UpdateOMS(){
        if (StatesOMS.statesOMS[currentArrayOMS][currentIndexOMS].execute()){
            TimeOMS = (float)Timer.getFPGATimestamp();
            isFirstOMS = true;
            currentIndexOMS ++;
        }
        SmartDashboard.putNumber("indexOMS", currentIndexOMS);
        SmartDashboard.putNumber("ArrayOMS", currentArrayOMS);
        SmartDashboard.putBoolean("endOMS", endOMS);
        SmartDashboard.putBoolean("startOMS", startOMS);

    }
}