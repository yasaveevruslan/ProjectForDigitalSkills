package frc.robot.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс предназначен для инциалилазации всего списка команд и вывода по ключу нужного массива для логики 
 * Данный класс содержит методы для работы cо структурой данных(HashMap)
 */
public class ActionIndex {
    
    //Исправлять что-то связанное с HashMap нельзя, но добавлять или удалять действия можно
    private final Map<String, Integer> actionKeysMap = new HashMap<>()
    {
        {
            put("FromStartToFirst", 0);
            put("greenPalateFirst", 1);
            put("redPalateFirst", 2);
            put("EndPalateFirst", 3);
            put("FromFirstToClean", 4);

            put("FromStartToSecond", 5);
            put("greenPalateSecond", 6);
            put("redPalateSecond", 7);
            put("EndPalateSecond", 8);
            put("FromSecondToClean", 9);

            put("FromStartToThird", 10);
            put("greenPalateThird", 11);
            put("redPalateThird", 12);
            put("EndPalateThird", 13);
            put("FromThirdToClean", 14);

            put("FromStartToFourth", 15);
            put("greenPalateFourth", 16);
            put("redPalateFourth", 17);
            put("EndPalateFourth", 18);
            put("FromFourthToClean", 19);

            put("Clean", 20);
            put("FromStartToWarehouse", 21);
            put("FromWarehouseToFirst", 22);
            put("FromWarehouseToSecond", 23);
            put("FromWarehouseToThird", 24);
            put("FromWarehouseToFourth", 25);

            put("Warehouse1", 26);
            put("Warehouse2", 27);
            put("Warehouse3", 28);
            put("Warehouse4", 29);
            put("Warehouse5", 30);

            put("CubeOut", 31);
            put("CubeClean", 32);
            put("CubePut", 33);
            put("CubeIn", 34);
            put("CubeTakeInRobot", 35);
            put("CubeCleanInRobot", 36);
            put("ToCleanLift", 37);

            put("END", 38);
        }
    };

    /**
     * Метод предназначен для получения номера массива, в котором содержиться текующие действие для логики
     */
    
    public int getIndexForActionMap(String action) 
    {
        return actionKeysMap.getOrDefault(action, 38);
    }
}
