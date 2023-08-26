package frc.robot.subsystems.cases;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Function;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.States;
import frc.robot.subsystems.Training;

public class Sensors implements IState
{

    private float x, y, z;
    private SmoothEnum mode;
    private String element;

    private float gyro = 0;

    private float timeEnd, mbTime = 0;

    private Training train = RobotContainer.train;

    private float[][] functionSpeedX = { { 0f, 1f, 4f, 8F, 10f, 18f, 24f, 40 }, { 0f, 5f, 8f, 16, 30f, 46, 64f, 90f }};
    private float[][] functionSpeedY = { { 0f, 1f, 2.5f, 6.5F, 10f, 18f, 24f, 40 }, { 0f, 6f, 12f, 17, 29f, 46f, 64f, 90f }};

    private float[][] functionSpeedXcolib = { { 0f, 1f, 2f, 5f, 10f, 18f }, { 0f, 5f, 10f, 12f, 18f, 36f }, };
    private float[][] functionSpeedYcolib = { { 0f, 1f, 2f, 5f, 10f, 18f }, { 0f, 6f, 13f, 15f, 18f, 36f }, };

    private float[][] speedZfunc = { { 0f, 1.2f, 3f, 6f, 12f, 26, 32, 50 }, { 0f, 6f, 12f, 19, 24f, 32, 48, 70 } };
    private float[][] functionSpeedZ = { { 0f, 1f, 2f, 4.5f, 8f, 10f, 18f }, { 0f, 6f, 12, 19, 25f, 30f, 52f }};
    private float[][] timeSpeed = {{0f, 0.15f, 0.3f, 0.5f, 0.8f}, {0f, 0.3f, 0.5f, 0.7f, 1}};


    public Sensors(float x, float y, String element, float z, SmoothEnum mode)
    {
        this.x = x;
        this.y = y;
        this.element = element;
        this.z = z;
        this.mode = mode;
    }
    
    @Override
    public boolean execute() 
    {
        return SensorsDrive(this.x, this.y, this.element, this.z, this.mode);
    }

    private static final float DECELERATION_XY = 0.3f, NOT_DECELERATION_XY = 25;
    private static final float DECELERATION_Z = 0.35f, NOT_DECELERATION_Z = 10;

    private float cofX = 0.3f, cofZ = 0.35f, acc = 1;
    private boolean smooth = false;

    private boolean mbX = false;
    private boolean mbY = false;
    private boolean mbZ = false;

    private float[][] zTransFunction;

    private float[][] functionX, functionY;
    
    
    private boolean SensorsDrive(float x, float y, String element, float z, SmoothEnum mode)
    {

        if (StateMachine.isFirst)
        {
            gyro = train.getYaw();
            StateMachine.isFirst = false;
        }

        generationSmooth(mode);

        

        float newX = axisX();
        float newY = axisY();
        float newZ = axisZ();

        float speedX = Function.TransF(this.functionX, newX) * this.acc;
        float speedY = Function.TransF(this.functionY, newY) * this.acc;
        float speedZ = Function.TransF(this.zTransFunction, newZ) * this.acc;

        boolean stopX = Function.InRangeBool(newX, -this.cofX, this.cofX);
        boolean stopY = Function.InRangeBool(newY, -this.cofX, this.cofX);
        boolean stopZ = Function.InRangeBool(newZ, -this.cofZ, this.cofZ);

        mbX = Function.InRangeBool(newX, -0.7f, 0.7f);
        mbY = Function.InRangeBool(newY, -0.7f, 0.7f);
        mbZ = Function.InRangeBool(newZ, -0.7f, 0.7f);

        SmartDashboard.putBoolean("stopXsens", stopX);
        SmartDashboard.putBoolean("stopYsens", stopY);
        SmartDashboard.putBoolean("stopZsens", stopZ);

        if (stopX && stopY && stopZ && !this.smooth)
        {
            train.setAxisSpeed(0, 0, 0, false);
        }
        else
        {
            train.setAxisSpeed(speedX, speedY, speedZ, this.smooth);
        }
        
        return end(stopX && stopY && stopZ, this.smooth);
        
    }

    private float axisX()
    {
        if (this.x > 0)
        {
            return train.getFrontSonic() - this.x;
        }
        else
        {
            return 0;
        }
    }

    private float axisY()
    {
        if (this.y == 0.0f)
        {
            return 0;
        }
        else if (this.element.equals(States.USE_FRONT_SHARP))
        {
            return Math.abs(this.y) - train.getFrontSharp();
        }
        else if(this.element.equals(States.USE_BACK_SHARP))
        {
            return Math.abs(this.y) - train.getBackSharp();
        }
        else if (this.element.equals(States.USE_RIGHT_SONIC))
        {
            return train.getRightSonic() - this.y;
        }
        else
        {
            return 0;
        }

    }

    private float axisZ()
    {
        float dicZ = 0;
        if (this.z == 1)
        {
            if ((train.getFrontSharp() + train.getBackSharp()) / 2 < 25)
            {
                this.zTransFunction = this.functionSpeedZ;
                dicZ = train.getBackSharp() - train.getFrontSharp();
            }
            else
            {
                this.zTransFunction = this.speedZfunc;
                if (Function.InRangeBool(this.gyro - train.getYaw(), -0.7f, 0.7f))
                {
                    dicZ = 0;
                }
                else
                {
                    dicZ = this.gyro - train.getYaw();
                }
            }
        }
        else
        {
            this.zTransFunction = this.speedZfunc;
            if (Function.InRangeBool(this.gyro - train.getYaw(), -0.7f, 0.7f))
            {
                dicZ = 0;
            }
            else
            {
                dicZ = this.gyro - train.getYaw();
            }
        }
        return dicZ;
    }

    private boolean end(boolean axis, boolean smooth)
    {


        if (axis)
        {
            return Timer.getFPGATimestamp() - this.timeEnd > 0.2f;
        }
        else
        {
            this.timeEnd = (float)Timer.getFPGATimestamp();
            if (this.mbX && this.mbY && this.mbZ)
            {
                return Timer.getFPGATimestamp() - this.mbTime > 2.5f;
            }
            else
            {
                this.mbTime = (float)Timer.getFPGATimestamp();
                return false;
            }
        }


    }

    private void generationSmooth(SmoothEnum mode)
    {
        if (mode == SmoothEnum.OFF)
        {
            this.cofX = Sensors.DECELERATION_XY;
            this.cofZ = Sensors.DECELERATION_Z;
            this.smooth = false;
            this.acc = Function.TransF(timeSpeed, (float)Timer.getFPGATimestamp() - StateMachine.StartTime);
            this.functionX = this.functionSpeedXcolib;
            this.functionY = this.functionSpeedYcolib;
        }
        else if(mode == SmoothEnum.ACCELERATION)
        {
            this.cofX = Sensors.NOT_DECELERATION_XY;
            this.cofZ = Sensors.NOT_DECELERATION_Z;
            this.smooth = true;
            this.acc = Function.TransF(timeSpeed, (float)Timer.getFPGATimestamp() - StateMachine.StartTime);
            this.functionX = this.functionSpeedX;
            this.functionY = this.functionSpeedY;
        }
        else if (mode == SmoothEnum.FAST)
        {
            this.cofX = Sensors.NOT_DECELERATION_XY;
            this.cofZ = Sensors.NOT_DECELERATION_Z;
            this.smooth = true;
            this.acc = 1;
            this.functionX = this.functionSpeedX;
            this.functionY = this.functionSpeedY;
        }
        else
        {
            this.cofX = Sensors.DECELERATION_XY * 2.5f;
            this.cofZ = Sensors.DECELERATION_Z * 2f;
            this.smooth = true;
            this.acc = 1;
            this.functionX = this.functionSpeedX;
            this.functionY = this.functionSpeedY;
        }
    }

}