package frc.robot.subsystems;

/**
 * Данный класс выполняет процесс фильтрации входных данных,
 * который направлен на снижение влияния шумов,
 * приводя к изменению выходных данных.
 */

public class MeanFilter 
{
    private float[] arrayForFilter;
    private int filterPowerInit = 10;

    // Конструктор для создания массива дефолтного размера со значениями 
    public MeanFilter()
    {
        this.arrayForFilter = new float[this.filterPowerInit];
    }

    // Конструктор для создания массива желаемого размера со значениями 
    public MeanFilter(int filterPower)
    {
        this.filterPowerInit = filterPower;
        this.arrayForFilter = new float[this.filterPowerInit];
    }

    // Сама реализация фильтра
    public float Filter(float val)
    {
        for (int i = filterPowerInit - 1; i > 0; i--)
        {
            this.arrayForFilter[i] = this.arrayForFilter[i - 1];
        }
        this.arrayForFilter[0] = val;

        float sum = 0;
        for (int i = 0; i < this.arrayForFilter.length; ++i)
        {
            sum += this.arrayForFilter[i];
        }
        return sum / this.arrayForFilter.length;
    }

}