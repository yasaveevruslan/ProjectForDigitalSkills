package frc.robot.logic;
/**
 * Класс предназначен для реализации комманд для логики
 */
public class DriveElements {

    private String action;
    private int positionLift;

    public DriveElements(String actionIn, int positionLiftIn)
    {
        this.action = actionIn;
        this.positionLift = positionLiftIn;
    }

    public String getAction() 
    {
        return action;
    }

    public int getPositionLift() 
    {
        return positionLift;
    }

}

