package frc.robot.subsystems.cases;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.States;
import frc.robot.subsystems.Training;

/**
 * Данный класс предназначен для установки комманд связанные с работой датчиком черных линий
 */
public class BlackLineSensor implements IState
{

    private Training train = RobotContainer.train;
    private float distanceY;
    private int position;
    private String name;

    private boolean end = false;
    private float timeEnd = 0;

    public static int positionBlackLine = 0;
    private boolean flag = true, signal;

    private float coordinateX, coordinateY, coordinateZ = 0;

    public BlackLineSensor(String name, float distanceY, int position)
    {
        this.name = name;
        this.distanceY = distanceY;
        this.position = position;
    }

    /**
     * Данный метод предназначен для выбора метода, который будет выполняться в зависимости от вашей комманды
     */
    
    @Override
    public boolean execute() 
    {
        switch (this.name)
        {  
            case States.SEARCH_BLACK_LINE:
            this.end = scanBlackLine(this.distanceY);
            break;

            case States.POSITION_BLACK_LINE:
            this.end = this.positionBlackLine(this.position);
            break;

            case States.RESET_BLACK_LINE:
            this.end = this.resetBlackLinePosition(this.position);
            break;

        }
        return this.end;
    }




    private float[][] speedXandYfunc = {{0f, 5f, 15f, 30f, 70f, 120f},{0, 3f, 5f, 10f, 18f, 45f}}; //REL
    private float[][] speedZfunc = {{0, 1.2f, 3f, 6f, 12f, 26f},{0f, 4f, 10f, 18, 24f, 32}}; //REL
    private float[][] timeSpeed = {{0f, 0.15f, 0.3f, 0.5f, 0.8f}, {0f, 0.3f, 0.5f, 0.7f, 1}};



    private boolean scanBlackLine(float distanceY){
        
        float gyro = RobotContainer.train.getYaw();
        float[] polar = Function.ReImToPolar(0, distanceY);
        float[] coordinates = Function.PolarToReIm(polar[0], (float) (polar[1] + Math.toRadians(gyro)));
        
        if (StateMachine.isFirst)
        {
            coordinateX = coordinates[0] + train.positionX;
            coordinateY = coordinates[1] + train.positionY;
            coordinateZ = 0 + gyro;
        }
        float acc = Function.TransF(timeSpeed, (float)Timer.getFPGATimestamp() - StateMachine.StartTime);


        float nowX = coordinateX - train.positionX;
        float nowY = coordinateY - train.positionY;
        float nowZ = coordinateZ - train.getYaw();


        float r = Function.TransF(speedXandYfunc, (float) Math.sqrt(nowX * nowX + nowY * nowY));
        float theta = (float) (Math.atan2(nowY, nowX) - Math.toRadians(train.getYaw()));


        float speedX = (float)(r * Math.cos(theta)) * acc;
        float speedY = (float)(r * Math.sin(theta)) * acc;
        float speedZ = Function.TransF(speedZfunc, nowZ) * acc;


        boolean stopX = Function.InRangeBool(nowX, -5, 5);
        boolean stopY = Function.InRangeBool(nowY, -5, 5);
        boolean stopZ = Function.InRangeBool(nowZ, -0.5f, 0.5f);
        

        boolean end = stopX && stopY && stopZ;
        boolean signal =  train.getBlackLineLeft() <= 9 || train.getBlackLineRight() <= 9;

        if (end || signal)
        {
            train.setAxisSpeed(speedX, speedY, speedZ, false);
        }
        else
        {
            this.timeEnd = (float) Timer.getFPGATimestamp();
            train.setAxisSpeed(speedX, 0, speedZ, false);

        }
        return (end || signal) && (Timer.getFPGATimestamp() - this.timeEnd > 0.3f);
    }



    private boolean positionBlackLine(int position)
    {
        float gyro = RobotContainer.train.getYaw();
        float[] polar = Function.ReImToPolar(0, 0);
        float[] coordinates = Function.PolarToReIm(polar[0], (float) (polar[1] + Math.toRadians(gyro)));
        
        if (StateMachine.isFirst)
        {
            coordinateX = coordinates[0] + train.positionX;
            coordinateY = coordinates[1] + train.positionY;
            coordinateZ = 0 + gyro;

            positionBlackLine = 0;
            // if (position >= positionBlackLine && positionBlackLine != 0)
            // {
            //     positionBlackLine --;
            // }
            StateMachine.isFirst = false;
        }

        float acc = Function.TransF(this.timeSpeed, (float)Timer.getFPGATimestamp() - StateMachine.StartTime);

        float nowZ = this.coordinateZ - train.getYaw();
        float speedZ = Function.TransF(this.speedZfunc, nowZ) * acc;

        // this.signal = train.getBlackLineRight() < 10f;
        this.signal = train.getBlackLineLeft() < 8f;

        float speedY = 0;

        if (position == positionBlackLine)
        {
            speedY = train.getBlackLineLeft() - train.getBlackLineRight() * 4;
        }
        else if (position - positionBlackLine > 0)
        {
            speedY = -25;
        }
        else
        {
            speedY = 15;
        }



        if (this.signal && this.flag)
        {
            if (position - positionBlackLine > 0)
            {
                positionBlackLine++; 
            }
            else
            {
                positionBlackLine--; 
            }

            this.flag = false; 
        }
        if (!this.signal && !this.flag)
        {
            this.flag = true; 
        }

        // boolean stopY = train.getBlackLineLeft() < 8 /*&& train.getBlackLineRight() <= 10*/ && position == positionBlackLine;
        boolean stopY = train.getBlackLineLeft() < 8 && train.getBlackLineRight() <= 10 && position == positionBlackLine;

        if (stopY)
        {
            train.setAxisSpeed(0, 0, 0, false);
        }
        else{   
            train.setAxisSpeed(0, speedY, speedZ, false);
        }
        

        return stopY;
    }

    private boolean resetBlackLinePosition(int newPosition)
    {
        positionBlackLine = newPosition;
        train.setAxisSpeed(0, 0, 0, false);
        return Timer.getFPGATimestamp() - StateMachine.StartTime > 0.5f;
    }
}