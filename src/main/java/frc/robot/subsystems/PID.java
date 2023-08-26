package frc.robot.subsystems;

/** 
 * Класс выполняет реализацию контроллера с пропорционально-интегрально-дифференциальным (ПИД) регулированием.
 */

public class PID
{
    private float p, i, d, minLimit, maxLimit;

    private float kp, ki, kd;

    private boolean reset = false;

    private float cofEnc = 0.9f, output = 0, error = 0;

    public float setpoint;

    // Конструктор для дефолтных значений
    public PID()
    {
        this(0.35f, 0.095f, 0.0001f, -100, 100);
    }

    // Конструктор для желаемых значений
    public PID(float KP, float KI, float KD, float min, float max)
    {
        this.kp = KP;
        this.ki = KI;
        this.kd = KD;
        checkLimit(min, max);
    }

    // Проверка на ограничения
    private void checkLimit(float min, float max)
    {
        minLimit = -100;
        maxLimit = 100;
        if (min < max)
        {
            minLimit = min;
            maxLimit = max;
        }
        else if (min > max)
        {
            minLimit = max;
            maxLimit = min;
        }
    }

    // Реализация ПИДа для ваших моторов
    public float calculatePID(float process)
    {
        if (reset)
        {
            this.p = 0;
            this.i = 0;
            this.d = 0;
            this.reset = false;
        }
        else
        {
            process *= cofEnc;
            this.error = setpoint -(-process);
            this.p = this.error * this.kp;
            this.i += this.p * this.ki;
            this.d += this.i * this.kd;
        }

        this.output = Function.InRange(p + i + d, minLimit, maxLimit);

        return output;
    }

    public void resetPID()
    {
        this.p = 0;
        this.i = 0;
        this.d = 0;
        this.reset = true;
    }
}
