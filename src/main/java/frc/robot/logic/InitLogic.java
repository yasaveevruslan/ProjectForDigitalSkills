package frc.robot.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Класс отвечает за создание команд для передвижения и лифта для робота
 */
public class InitLogic
{

    public static ArrayList<DriveElements> driveElements = new ArrayList<>();
    public ArrayList<int[]> indexMas = new ArrayList<>();

    public String[][] orders = new String[][]{

            {"none", "none", "none", "none", "none", "none", "none"},
            {"white", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none"}

    };

    public String[][] warehouse = new String[][]{
            { "white", "white", "white", "white", "white" },
            { "blue", "blue", "blue", "blue", "blue" },
            { "yellow", "yellow", "yellow", "yellow", "yellow" }


    };
    private final String[] palace = {"First", "Second", "Third", "Fourth"};
    public static String[][] stands = new String[][] {
            { "green", "red" },
            { "green", "red" },
            { "green", "red" },
            { "green", "red" } };


    private String commandPath;
    private int valueRetGreen = 0;

    public InitLogic(String command)
    {
        this.commandPath = command;
    }

    /**
     * Запуск создания логики для "модуля В" и сразу выполняется генерация комманд и нужных массивов
     */

    public void initCmodule()
    {
        SettingsForLogic set = new SettingsForLogic(warehouse,orders);
        ArrayList<String> arr = set.getCommands();

        if(commandPath.equals("warehouse"))
        {
            setWarehousePath(arr);
        }else if (commandPath.equals("ret")){
            ArrayList<String> retR = new ArrayList<>();
            ArrayList<String> retG = new ArrayList<>();
            for (String command: arr)
            {
                if(command.split(" ")[0].equals("RET"))
                {
                    if(command.split(" ")[2].equals("yellow"))
                    {
                        retR.add(command);
                    }
                    else
                    {
                        retG.add(command);
                        valueRetGreen+=1;
                    }
                }
            }
            Collections.reverse(retR);

            setRetPathGreenC(retG);
            setRetPathRedC(retR);
        }else if(commandPath.equals("delivery")){
            ArrayList<String> delR = new ArrayList<>();
            ArrayList<String> delG = new ArrayList<>();

            for (String command: arr)
            {
                if(command.split(" ")[0].equals("DEL"))
                {
                    if(command.split(" ")[2].equals("yellow"))
                    {
                        delR.add(command);
                    }
                    else
                    {
                        delG.add(command);
                    }
                }
            }

            setDeliveryPathGreenC(delG);
            setDeliveryPathRedC(delR);
        }
        driveElements.add(new DriveElements("END", 0));

        for (DriveElements element : driveElements)
        {
            System.out.println(element.getAction() + " " + element.getPositionLift());
        }

        generationLogic();

        for(int[] index: indexMas)
        {
            System.out.println(Arrays.toString(index));

        }
    }

    /**
     * Генерация маршрутов для вывоза синих и белых кубов для модуля В
     */

    private void setRetPathGreenC(ArrayList<String> ret)
    {
        if(!ret.isEmpty()){
            for (int i = 0; i < ret.size(); i++)
            {
                String command = ret.get(i);
                int indexPalate = Arrays.asList(palace).indexOf(command.split(" ")[1]);
                if (driveElements.isEmpty())
                {
                    driveElements.add(new DriveElements("FromStartTo"+command.split(" ")[1],0));
                    driveElements.add(new DriveElements("EndPalate"+command.split(" ")[1],0));
                    driveElements.add(new DriveElements(stands[indexPalate][0]+"Palate"+command.split(" ")[1],0));
                }

                driveElements.add(new DriveElements("CubeOut",Integer.parseInt(command.split(" ")[3])));
                if(command.equals(ret.get(ret.size()-1)))
                {
                    driveElements.add(new DriveElements("From"+command.split(" ")[1]+"ToClean",0));
                    driveElements.add(new DriveElements("ToCleanLift", 0));
                    driveElements.add(new DriveElements("CubeCleanInRobot", 0));
                    for (int j = 1; j < valueRetGreen; j++)
                    {
                        driveElements.add(new DriveElements("CubeClean",0));
                    }

                }
            }
        }
    }
    /**
     * Генерация маршрутов для вывоза жёлтых кубов для модуля В
     */

    private void setRetPathRedC(ArrayList<String> ret)
    {
        for (String command : ret) {
            int indexPalate = Arrays.asList(palace).indexOf(command.split(" ")[1]);
            if (driveElements.isEmpty()) {
                driveElements.add(new DriveElements("FromStartTo" + command.split(" ")[1], 0));

                driveElements.add(new DriveElements("EndPalate" + command.split(" ")[1], 0));
                driveElements.add(new DriveElements(stands[indexPalate][1] + "Palate" + command.split(" ")[1], 0));
            }

            driveElements.add(new DriveElements("CubeOut", Integer.parseInt(command.split(" ")[3])));
            driveElements.add(new DriveElements("From" + command.split(" ")[1] + "ToClean", 0));
            driveElements.add(new DriveElements("ToCleanLift", 0));
            driveElements.add(new DriveElements("CubeCleanInRobot", 0));
            driveElements.add(new DriveElements("Clean", 0));
        }
    }

    /**
     * Генерация маршрутов для ввоза жёлтых кубов для модуля В
     */

    private void setDeliveryPathRedC(ArrayList<String> del)
    {
        SettingsForLogic set = new SettingsForLogic(warehouse,orders);

        for (String command : del) {
            int indexPalate = Arrays.asList(palace).indexOf(command.split(" ")[1]);

            if (driveElements.isEmpty()) {
                driveElements.add(new DriveElements("FromStartToWarehouse", 0));
            }

            String[] coordinate = set.getCubeForWarehouse(command.split(" ")[2]).split(" ");
            driveElements.add(new DriveElements(coordinate[0], Integer.parseInt(coordinate[1])));

            driveElements.add(new DriveElements("FromWarehouseTo" + command.split(" ")[1], 0));
            driveElements.add(new DriveElements("CubeTakeInRobot", 0));
            driveElements.add(new DriveElements("EndPalate" + command.split(" ")[1], 0));
            driveElements.add(new DriveElements(stands[indexPalate][1] + "Palate" + command.split(" ")[1], 0));
            driveElements.add(new DriveElements("CubePut", Integer.parseInt(command.split(" ")[3])));
        }

    }

    /**
     * Генерация маршрутов для ввоза синих и белых кубов для модуля В
     */

    private void setDeliveryPathGreenC(ArrayList<String> del)
    {
        SettingsForLogic set = new SettingsForLogic(warehouse,orders);

        for (String command : del) {
            int indexPalate = Arrays.asList(palace).indexOf(command.split(" ")[1]);
            if (driveElements.isEmpty()) {
                driveElements.add(new DriveElements("FromStartToWarehouse", 0));
            }

            String[] coordinate = set.getCubeForWarehouse(command.split(" ")[2]).split(" ");
            driveElements.add(new DriveElements(coordinate[0], Integer.parseInt(coordinate[1])));

            driveElements.add(new DriveElements("FromWarehouseTo" + command.split(" ")[1], 0));
            driveElements.add(new DriveElements("CubeTakeInRobot", 0));
            driveElements.add(new DriveElements("EndPalate" + command.split(" ")[1], 0));
            driveElements.add(new DriveElements(stands[indexPalate][0] + "Palate" + command.split(" ")[1], 0));
            driveElements.add(new DriveElements("CubePut", Integer.parseInt(command.split(" ")[3])));
        }
    }

    /**
     * Генерация маршрутов для работы со складом в модуле В
     */

    private void setWarehousePath(ArrayList<String> path){
        SettingsForLogic set = new SettingsForLogic(warehouse,orders);

        String[] coordinate = set.getCubeForWarehouse(path.get(path.size() - 1).split(" ")[2]).split(" ");
        driveElements.add(new DriveElements(coordinate[0], Integer.parseInt(coordinate[1])));
    }

    /**
     * Метод для генерации нужных массивов для логики
     */
    
    public void generationLogic() {
        ActionIndex mapper = new ActionIndex();

        for (DriveElements s : driveElements) {
            int[] a = new int[]{mapper.getIndexForActionMap(s.getAction()), s.getPositionLift()};
            indexMas.add(a);
        }
    }

}

