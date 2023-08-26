package frc.robot.subsystems;

/**
 * Данный класс содержит методы для работы со значениями, диапазонами(массивы данных)
 */
public class Function 
{
    private static float[] speeds = new float[2];

    /**
     * Метод предназначен для вычисления значений,
     * находящихся в заданными диапазонами входных данных и соответствующими выходными значениями
     */
    
    public static float TransF(float[][] values, float value)
    {
        float result = 0;
        float max_o;
        float max_i;
        float min_o;
        float min_i;

        boolean minus = false;
        
        if (value < 0) 
        {
            minus = true;
            value = Math.abs(value);
        }

        if (value >= values[0][values[0].length - 1]) 
        {
            result = values[1][values[1].length - 1];
        }
        else 
        {
            for (int i = 0; i < values[0].length; i++) 
            {
                if (value >= values[0][i] && value <= values[0][i + 1])
                {
                    min_i = values[0][i + 1];
                    max_i = values[0][i];
                    min_o = values[1][i + 1];
                    max_o = values[1][i];

                    if (value == values[0][i]) 
                    {
                        result = values[1][i];
                        break;
                    }
                    else 
                    {
                        result = min_o + (((max_o - min_o) * ((Math.abs(value) - min_i) * 100 / (max_i - min_i))) / 100);
                
                    }
                }
            }
        }

        if (minus)
        {
            result *= -1;
        }
        return result;
    }

    /**
     * Метод позволяет выполнить преобразование комплексного числа 
     * из декартовой системы координат в полярную,предоставляя его радиус и угол.
     */

    public static float[] ReImToPolar(float x, float y)
    {
        float[] arr = new float[2];
        arr[0] = (float)Math.sqrt((x * x + y * y));  // вычисление радиуса комплексного числа
        arr[1] = (float)Math.atan2(y, x);  // угол полярных координат комплексного числа
        return arr; 
    }

    public static float[] PolarToReIm(float r, float theta)
    {
        float[] arr = new float[2];

        arr[0] = r * (float)Math.cos(theta);  // x
        arr[1] = r * (float)Math.sin(theta);  // y

        return arr;
    }

    public static float[] smoothDrive(float x, float y, float cof)
    {
        speeds[0] += InRange(x - speeds[0], -cof, cof);
        speeds[1] += InRange(y - speeds[1], -cof, cof);

        return speeds;
    }
    
    public static int axis(float x, float y, float curX, float curY)
    { 
        float xDist = 0; 
        if (Math.abs(x - curX) != 0)
        { 
            xDist = Math.abs(x - curX); 
        } 
        float yDist = Math.abs(y - curY); 
 
        if (Function.InRangeBool(yDist / xDist, 0.75f, 1.38f) || (xDist < 150 && yDist < 150))
        { 
            return 0; 
        }
        else
        { 
            if (yDist / xDist > 1)
            { 
                return 1; 
            }
            else
            { 
                return 2; 
            }

        }
    }

    /**
     * Метод позволяет выполнить ограничения входных данных.
     * Вывод ограниченных данных
     */

    public static float InRange(float in, float min, float max)
    {
        return in < min ? min : in > max ? max : in;
    }

    /**
     * Метод позволяет выполнить ограничения входных данных.
     * Вывод проверки нахождения входных данных
     */
    
    public static boolean InRangeBool(float in, float min, float max)
    {
        return in >= min && in <= max;
    }
}
