package frc.robot.subsystems.cases;

import java.util.Arrays;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.States;
import frc.robot.subsystems.Training;

public class Odometry implements IState
{
    public Training train = RobotContainer.train;

    private String name;
    private float x, y, z;
    private SmoothEnum mode;

    private float coordinateX, coordinateY, coordinateZ = 0;

    private float[][] masFirst;
    private float[][] masSecond;

    private boolean relative = false;

    @Override
    public boolean execute()
    {
        return this.WhatIsElement(this.name, this.x, this.y, this.z, this.mode);
    }
    
    public Odometry(String name, float x, float y, float z, SmoothEnum mode)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.mode = mode;
    }

    private float[][] speedXandYfunc = {{0f, 5f, 15f, 40f, 70f, 150f, 170f, 220f}, {0f, 5f, 10f, 15f, 28f, 50f, 70f, 95}}; //ABS
    private float[][] speedZfunc = { { 0f, 1.2f, 3f, 6f, 12f, 26, 32, 50 }, { 0f, 4f, 10f, 18, 24f, 32, 48, 70 } };//ABS

    private float[][] speedXandYfuncRel = {{0f, 5f, 15f, 30f, 70f, 120f},{0, 3f, 5f, 10f, 18f, 45f}}; //REL
    private float [][] speedZfuncRel = {{0, 1.2f, 3f, 6f, 12f, 26f},{0f, 4f, 10f, 18, 24f, 32}}; //REL

    private boolean WhatIsElement(String name, float x, float y, float z, SmoothEnum mode)
    {

        float gyro = RobotContainer.train.getYaw();
        float[] polar = Function.ReImToPolar(x, y);
        float[] coordinates = Function.PolarToReIm(polar[0], (float) (polar[1] + Math.toRadians(gyro)));

        if (name.equals(States.ABSOLUTE_ODOMETRY))
        {
            coordinateX = x;
            coordinateY = y;
            coordinateZ = z;
            masFirst = Arrays.copyOf(speedXandYfunc, speedXandYfunc.length);
            masSecond = Arrays.copyOf(speedZfunc, speedZfunc.length);
            relative = false;
        }
        else
        {
            if (StateMachine.isFirst)
            {
                coordinateX = coordinates[0] + train.positionX;
                coordinateY = coordinates[1] + train.positionY;
                coordinateZ = z + gyro;

                relative = true;
            }                
            masFirst = Arrays.copyOf(speedXandYfuncRel, speedXandYfuncRel.length);
            masSecond = Arrays.copyOf(speedZfuncRel, speedZfuncRel.length);
        }
        return Drive(coordinateX, coordinateY, coordinateZ, mode, masFirst, masSecond, relative);
    }

    private float[][] timeSpeed = {{0f, 0.15f, 0.3f, 0.5f, 0.8f}, {0f, 0.3f, 0.5f, 0.7f, 1}};

    private int position = 0;
    private float cofX = 5, cofZ = 0.5f, acc = 1;
    private boolean smooth = false;

    private static final float DECELEARTION_XY = 5, NOT_DECELERATION_XY = 220;
    private static final float DECELEARTION_Z = 0.5f, NOT_DECELERATION_Z = 15;

    private boolean Drive(float x, float y, float z, SmoothEnum mode, float[][] masFirst, float[][] masSecond, boolean Relative)
    {

        float nowX = x - train.positionX;
        float nowY = y - train.positionY;
        float nowZ = z - RobotContainer.train.getYaw();

        if (StateMachine.isFirst)
        {
            if (!Function.InRangeBool(nowZ, -30, 30))
            {
                position = Function.axis(x, y, train.positionX, train.positionY);
            }
            else
            {
                position = 0;
            }
            StateMachine.isFirst = false;
        }
        else if (Function.InRangeBool(nowZ, -10, 10))
        {
            position = 0;
        }

        if (mode == SmoothEnum.OFF)
        {
            cofX = Odometry.DECELEARTION_XY;
            cofZ = Odometry.DECELEARTION_Z;
            smooth = false;
            acc = Function.TransF(timeSpeed, (float)Timer.getFPGATimestamp() - StateMachine.StartTime);
        }
        else if (mode == SmoothEnum.ACCELERATION)
        {
            cofX = Odometry.NOT_DECELERATION_XY;
            cofZ = Odometry.NOT_DECELERATION_Z;
            smooth = true;
            acc = Function.TransF(timeSpeed, (float)Timer.getFPGATimestamp() - StateMachine.StartTime);
        }
        else if (mode == SmoothEnum.FAST)
        {
            cofX = Odometry.NOT_DECELERATION_XY;
            cofZ = Odometry.NOT_DECELERATION_Z;
            smooth = true;
            acc = 1;
        }
        else
        {
            cofX = Odometry.DECELEARTION_XY * 2.5f;
            cofZ = Odometry.DECELEARTION_Z * 2.5f;
            smooth = true;
            acc = 1;
        }

        boolean stopX = Function.InRangeBool(nowX, -cofX, cofX);
        boolean stopY = Function.InRangeBool(nowY, -cofX, cofX);
        boolean stopZ = Function.InRangeBool(nowZ, -cofZ, cofZ);

        if (position == 1)
        {
            nowX *= 5.2f;
        }
        else if(position == 2)
        {
            nowY *= 5.2f;
        }        

        float r = Function.TransF(masFirst, (float) Math.sqrt(nowX * nowX + nowY * nowY));
        float theta = (float) (Math.atan2(nowY, nowX) - Math.toRadians(RobotContainer.train.getYaw()));

        float speedX = (float)(r * Math.cos(theta)) * acc;
        float speedY = (float)(r * Math.sin(theta)) * acc;
        float speedZ = Function.TransF(masSecond, nowZ) * acc;

        if (stopX && stopY && stopZ && !smooth)
        {
            RobotContainer.train.setAxisSpeed(0, 0, 0, false);
        }
        else
        {
            RobotContainer.train.setAxisSpeed(speedX, speedY, speedZ, smooth);
        }

        return stopX && stopY && stopZ;
    }
    
}