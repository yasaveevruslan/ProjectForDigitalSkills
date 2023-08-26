package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.cases.BlackLineSensor;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.Servo;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

/**
 * Данный класс предназначен для управления состовляющими робота
 */
public class Training extends SubsystemBase
{
    private TitanQuad motorRight, motorLeft, motorBack;
    private TitanQuad motorElevator;

    private TitanQuadEncoder encoderRight, encoderLeft, encoderBack;
    private TitanQuadEncoder encoderElevator;
    
    private float speedRight, speedLeft, speedBack;
    private float lastEncoderRight, lastEncoderLeft, lastEncoderBack;

    public PID RightPID = new PID();
    public PID LeftPID = new PID();
    public PID BackPID = new PID();
    public PID LiftPID = new PID();

    private float[][] transFunctionLift = {{0, 8, 20, 100, 200, 350}, {0, 5, 10, 20, 45, 70}};

    public float positionLift;
    private float speedLift;
    public boolean initializeLift = true, liftPosReached = false;

    private Servo grab, degrees;
    public float angleGrabServo = 195;
    public float angleDegreesServo;
    
    private AHRS gyro;
    private float yaw, newYaw;

    public float positionX, positionY;

    private AnalogInput sharpFront, sharpBack, blackLineRight, blackLineLeft;
    private Ultrasonic sonicFront, sonicRight;

    private MeanFilter sharpFrontMeanFilter, sharpBackMeanFilter, sonicFrontMeanFilter, sonicRightMeanFilter;
    private MedianFilter sharpFrontMedianFilter, sharpBackMedianFilter, sonicFrontMedianFilter, sonicRightMedianFilter;

    private Encoder buttonStart;
    private DigitalInput buttonEMS, limit;
    private DigitalOutput red, green;

    /**
     * Конструктор для установка портов в которые подключенны состовляющие робота
     * Выполнения потока установки скоростей на моторы, также установка углов на серваки
     */
    public Training()
    {
        motorRight = new TitanQuad(42, 1);
        motorLeft = new TitanQuad(42, 3);
        motorBack = new TitanQuad(42, 0);
        motorElevator = new TitanQuad(42, 2);

        encoderRight = new TitanQuadEncoder(motorRight, 1, 0.2399827721492203);
        encoderLeft = new TitanQuadEncoder(motorLeft, 3, 0.2399827721492203);
        encoderBack = new TitanQuadEncoder(motorBack, 0, 0.2399827721492203);
        encoderElevator = new TitanQuadEncoder(motorElevator, 2, 0.2399827721492203);

        gyro = new AHRS();

        grab = new Servo(9);
        degrees = new Servo(8);

        sharpFront = new AnalogInput(2);
        sharpBack = new AnalogInput(3);

        blackLineRight = new AnalogInput(0);
        blackLineLeft = new AnalogInput(1);

        sonicFront = new Ultrasonic(10, 11);
        sonicRight = new Ultrasonic(8, 9);

        sharpFrontMeanFilter = new MeanFilter(3);
        sharpBackMeanFilter = new MeanFilter(3);
        sonicFrontMeanFilter = new MeanFilter(8);
        sonicRightMeanFilter = new MeanFilter(8);

        sharpFrontMedianFilter = new MedianFilter();
        sharpBackMedianFilter = new MedianFilter();

        sonicFrontMedianFilter = new MedianFilter();
        sonicRightMedianFilter = new MedianFilter();

        buttonStart = new Encoder(0, 1);
        buttonEMS = new DigitalInput(2);
        limit = new DigitalInput(6);

        red = new DigitalOutput(12);
        green = new DigitalOutput(13);

        new Thread( () -> {
            while(!Thread.interrupted())
            {
                try 
                {
                    this.setSpeedsDriveMotors(this.speedRight, this.speedLeft, this.speedBack);

                    setElevator();  
                    setGrabServo(getButtonEMS());
                    setDegreesServo(getButtonEMS());
                    
                    Thread.sleep(5);
                } 
                catch (Exception e) 
                {
                    // e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Метод для получения значения с передней IR датчика
     */

    // private float[][] coefSharpFront = {{8.5f, 10},{0, 0.3f}};
    public float getFrontSharp()
    {
        float value = (sharpFrontMeanFilter.Filter(sharpFrontMedianFilter.Filter((float) (Math.pow(sharpFront.getAverageVoltage(), -1.2045) * 27.726))));
        // float coef = Function.TransF(coefSharpFront, value);
        return (value);
    }

    /**
     * Метод для получения значения с задней IR датчика
     */

    // private float[][] coefSharpBack = {{8.5f, 15, 20 , 25},{0.1f, 0.8f, 1.5f, 2.0f}};
    public float getBackSharp()
    {
        float value = (sharpBackMeanFilter.Filter((sharpBackMedianFilter.Filter((float) (Math.pow(sharpBack.getAverageVoltage(), -1.2045) * 27.726)))));
        // float coef = Function.TransF(coefSharpBack, value);

        return (value);
    }

    /**
     * Методы для получения значения с датчика чёрной линии
     */

    public float getBlackLineRight()
    {
        return (float) (Math.pow(blackLineRight.getAverageVoltage(), -1.2045) * 27.726);
    }

    public float getBlackLineLeft()
    {
        return (float) (Math.pow(blackLineLeft.getAverageVoltage(), -1.2045) * 27.726);
    }

    /**
     * Метод для получения значения с переднего Sonic
     */

    public float getFrontSonic()
    {
        try 
        {
            sonicFront.ping();
            Timer.delay(0.005);

            return (sonicFrontMeanFilter.Filter((sonicFrontMedianFilter.Filter((float)(sonicFront.getRangeMM() / 10)))));
            // return (float)(sonicFront.getRangeMM() / 10);


        } catch (Exception e) 
        {
            return 10;
        }
    }

    /**
     * Метод для получения значения с правого Sonic
     */

    public float getRightSonic()
    {
        try 
        {
            sonicRight.ping();
            Timer.delay(0.005);

            return (sonicRightMeanFilter.Filter((sonicRightMedianFilter.Filter((float)(sonicRight.getRangeMM() / 10)))));
            // return (float)(sonicRight.getRangeMM() / 10);


        } catch (Exception e) 
        {
            return 10;
        }
    }

    /**
     * Метод для получения угла
     * Расчёты с использованием угла с гироскопа
     */
    
    public float getYaw()
    {
        this.yaw += (float)(gyro.getAngle() - this.newYaw);
        this.newYaw = (float) gyro.getAngle();
        return this.yaw;
    }


    /**
     * Метод для установки угла через вызов метода
     * И сброс до 0 угла
     */

    public void resetYaw(float angle)
    {
        this.newYaw = 0;
        this.yaw = angle;
        gyro.zeroYaw();
    }

    /**
     * Метод для установки угла через вызов метода
     */

    public void resetZeroYaw(float angle)
    {
        this.yaw = angle;
    }

    /**
     * Метод для установки скоростей на моторы базы робота
     */

    private void setSpeedsDriveMotors(float right, float left, float back)
    {

        motorRight.set(calculateSpeed(right, RightPID, encoderRight));
        motorLeft.set(calculateSpeed(left, LeftPID, encoderLeft));
        motorBack.set(calculateSpeed(back, BackPID, encoderBack));
    }

    /**
     * Метод для расчёта скорости на моторы базы робота
     */

    private float calculateSpeed(float speed, PID motorPID, TitanQuadEncoder encoder){
        if (speed == 0.0f ||getButtonEMS())
        {
            motorPID.resetPID();
            speed = 0.0f;
        }
        else
        {
            motorPID.setpoint = speed;
            speed = motorPID.calculatePID((float) encoder.getSpeed()/10) / 100;
        }
        return speed;
    }

    /**
     * Методы для установки состояния ледов на роботе
     */

    public void setRed(boolean mode)
    {
        red.set(!mode);
    }

    public void setGreen(boolean mode)
    {
        green.set(mode);
    }

    /**
     * Метод для распределения скоростей на моторы
     */

    public void setAxisSpeed(float x, float y, float z, boolean mode)
    {
        
        float newX = x;
        float newY = y;
        float newZ = z;

        float[] speeds = Function.smoothDrive(x, y, 3.1f);

        if (mode)
        {
            newX = speeds[0];
            newY = speeds[1];
        }

        float right = newX - (newY / 2) + newZ;
        float left = -newX - (newY / 2) + newZ;
        float back = (newY) + newZ;

        float maxFirst = Math.max(Math.abs(right), Math.abs(left));
        float max = Math.max(maxFirst, Math.abs(back));

        float cof = 1;
        
        if (max > 98)
        {
            cof = 98 / max;
        }
        else
        {
            cof = 1;
        }

        speedRight = right * cof;
        speedLeft = left * cof;
        speedBack = back * cof;

    }

    /**
     * Метод для расчёта координат
     */

    public void CalculateOdometry()
    {

        float speedRight = (float) encoderRight.getEncoderDistance() - lastEncoderRight;
        float speedLeft = (float) encoderLeft.getEncoderDistance() - lastEncoderLeft;
        float speedBack = (float) encoderBack.getEncoderDistance() - lastEncoderBack;

        double radians = Math.toRadians(getYaw());

        float noneX = ((-speedLeft + speedRight) / 2) * 1.056f;
        float noneY = ((-speedRight - speedLeft + (speedBack * 2)) / 3) * 0.906f;

        float thetaR = (float)(Math.atan2(noneY, noneX) + radians);
        float r = (float)(Math.sqrt(noneY * noneY + noneX * noneX));

        positionX += (float)(r * Math.cos(thetaR));
        positionY += (float)(r * Math.sin(thetaR));

        lastEncoderRight = (float) encoderRight.getEncoderDistance();
        lastEncoderLeft = (float) encoderLeft.getEncoderDistance();
        lastEncoderBack = (float) encoderBack.getEncoderDistance();

    }


    /**
     * Сброс координат и энкодеров
     */

    public void resetCoordinates(float x, float y)
    {
        this.positionX = x;
        this.positionY = y;

        this.lastEncoderRight = 0;
        this.lastEncoderLeft = 0;
        this.lastEncoderBack = 0;
        
        encoderRight.reset();
        encoderLeft.reset();
        encoderBack.reset();
    }

    /**
     * Метод для работы с позициями лифта
     */

    public void setElevator()
    {

        if(initializeLift)
        {
            speedLift = 80;
            liftPosReached = getLimitSwitch();
        }
        else
        {
            speedLift = Function.TransF(transFunctionLift, (float) (positionLift - encoderElevator.getEncoderDistance()));
            liftPosReached = Function.InRangeBool((float) (positionLift - encoderElevator.getEncoderDistance()), -5, 5);
        }

        if ((liftPosReached && !getLimitSwitch()) || getButtonEMS())
        {
            LiftPID.resetPID();
            motorElevator.set(0);
        }
        else if(speedLift > 0 && getLimitSwitch())
        {
            LiftPID.resetPID();
            motorElevator.set(0);
            encoderElevator.reset();
        }
        else if(speedLift < 0 && encoderElevator.getEncoderDistance() < -730)
        {
            LiftPID.resetPID();
            motorElevator.set(0);
        }
        else
        {
            LiftPID.setpoint = speedLift;
            float lift = LiftPID.calculatePID((float) encoderElevator.getSpeed()/10);
            motorElevator.set(lift/100);

        }
    }

    /**
     * Методы для работы с серваками
     */
  
    private void setGrabServo(boolean disable)
    {
        if (getButtonEMS())
        {
            grab.setDisabled();
        }
        else 
        {
            grab.setAngle(this.angleGrabServo);
        }
    }

 

    public float getGrabServo()
    {
        return (float) grab.getAngle();
    }



    private void setDegreesServo(boolean disable)
    {
        if (disable)
        {
            degrees.setDisabled();
        }
        else
        {
            degrees.setAngle(this.angleDegreesServo + 15);
        }   
    }



    public float getDegreesServo()
    {
        return (float) degrees.getAngle();
    }


     /**
     * Метод для получения работы концевика
     * Обработка нажатия на концевик
     */

    public boolean getLimitSwitch()
    {
        try 
        {
            return limit.get();
        } 
        catch (Exception e) 
        {
            return false;
        }
    }

    /**
     * Метод для получения работы кнопки СТАРТ
     * Обработка нажатия на кнопку
     */

    public boolean getButtonStart()
    {
        try 
        {
            return (buttonStart.getDistance() == 1 || buttonStart.getDistance() == -2);
        }
        catch (Exception e) 
        { 
            return false;
        }
    }

    /**
     * Метод для получения работы кнопки СТОП
     * Обработка нажатия на кнопку
     */

    public boolean getButtonEMS(){
        try   
        {
            return buttonEMS.get();
        } 
        catch (Exception e) 
        {
            return false;
        }
    }

    /**
     * Метод для предназначен для вывода на панель
     */

    @Override
    public void periodic()
    {
        SmartDashboard.putNumber("FrontSonic", getFrontSonic());
        SmartDashboard.putNumber("RightSonic", getRightSonic());

        SmartDashboard.putNumber("FrontSharp", getFrontSharp());
        SmartDashboard.putNumber("BackSharp", getBackSharp());

        SmartDashboard.putNumber("BlackLineRight", getBlackLineRight());
        SmartDashboard.putNumber("BlackLineLeft", getBlackLineLeft());
        
        SmartDashboard.putNumber("PosX", positionX);
        SmartDashboard.putNumber("PosY", positionY);
        SmartDashboard.putNumber("Angle", getYaw());

        SmartDashboard.putNumber("StartValue", buttonStart.getDistance());

        SmartDashboard.putBoolean("StartButton", getButtonStart());
        SmartDashboard.putBoolean("EMSButton", getButtonEMS());
        SmartDashboard.putBoolean("LimitSwitch", getLimitSwitch());

        SmartDashboard.putNumber("degreesServo", getDegreesServo());
        SmartDashboard.putNumber("GrabServo", getGrabServo());

        SmartDashboard.putNumber("EncoderRight", encoderRight.getEncoderDistance());
        SmartDashboard.putNumber("EncoderLeft", encoderLeft.getEncoderDistance());
        SmartDashboard.putNumber("EncoderBack", encoderBack.getEncoderDistance());
        SmartDashboard.putNumber("EncoderLift", encoderElevator.getEncoderDistance());

        SmartDashboard.putNumber("positionBlackLine", BlackLineSensor.positionBlackLine);
    }

}