package frc.robot.subsystems;

import java.util.Arrays;

/**
 *Данный класс выполняет процесс фильтрации входных данных,
 *который направлен на сглаживание значений путём использования медианы,
 *приводя к изменению выходных данных.
 */

public class MedianFilter 
{
    private float[] arrayForFilter;
    private int filterPowerInit = 3;

    // Конструктор для создания массива дефолтного размера со значениями 
    public MedianFilter()
    {
        this.arrayForFilter = new float[this.filterPowerInit];
    }

    // Конструктор для создания массива желаемого размера со значениями 
    public MedianFilter(int filterPower)
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

        float[] copiedArr = Arrays.copyOf(this.arrayForFilter, this.filterPowerInit);
        quickSort(copiedArr, 0, this.filterPowerInit - 1);
        return copiedArr[this.filterPowerInit / 2];
    }

    //  Метод для выполнения сортировки для фильтра
    private void quickSort(float arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);
     
            quickSort(arr, begin, partitionIndex-1);
            quickSort(arr, partitionIndex+1, end);
        }
    }

    // Вспомогательный метод для разделения массива для быстрой сортировки
    private int partition(float arr[], int begin, int end) {
        float pivot = arr[end];
        int i = (begin-1);
     
        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;
     
                float swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
     
        float swapTemp = arr[i+1];
        arr[i+1] = arr[end];
        arr[end] = swapTemp;
     
        return i+1;
    }
}
