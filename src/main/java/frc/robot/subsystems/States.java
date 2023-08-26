package frc.robot.subsystems;

import frc.robot.subsystems.cases.BlackLineSensor;
import frc.robot.subsystems.cases.Camera;
import frc.robot.subsystems.cases.CmoduleSensors;
import frc.robot.subsystems.cases.End;
import frc.robot.subsystems.cases.IState;
import frc.robot.subsystems.cases.InitializeLogic;
import frc.robot.subsystems.cases.OMS;
import frc.robot.subsystems.cases.Odometry;
import frc.robot.subsystems.cases.Reset;
import frc.robot.subsystems.cases.Sensors;
import frc.robot.subsystems.cases.SmoothEnum;
import frc.robot.subsystems.cases.Start;
import frc.robot.subsystems.cases.Transition;

/**
 * Данный класс предназначен для прописания комманд роботу, также для запуска комманд для выполнения роботом
 */
public class States {

    public static final String ABSOLUTE_ODOMETRY = "absolute";
    public static final String RELATIVE_ODOMETRY = "relative";

    public static final String USE_FRONT_SHARP = "front";
    public static final String USE_BACK_SHARP = "back";
    public static final String USE_RIGHT_SONIC = "right";
    public static final String DEFAULT_SENSOR = "default";


    public static final String CAMERA_YELLOW = "Yellow";
    public static final String CAMERA_BLUE = "Blue";
    public static final String CAMERA_WHITE = "White";
    public static final String CAMERA_QR = "QR";
    public static final String CAMERA_QRC = "QRC";

    public static final String CAMERA_GREEN_CUBE = "GREEN";
    public static final String CAMERA_YELLOW_CUBE = "YELLOW";
    public static final String CAMERA_OBJECTS = "objects";
    public static final String CAMERA_OBJECTS_F = "objectsF";
    public static final String CAMERA_STANDS = "stands";

    public static final String CAMERA_COLOR_STANDS_F = "colorStandsF";
    public static final String CAMERA_COLOR_STANDS = "colorStands";

    public static final String ALL_RESET = "all";
    public static final String GYRO_RESET = "gyro";

    public static final String C_SONIC = "sonic";
    public static final String C_SHARP = "sharp";

    public static final String SEARCH_BLACK_LINE = "search";
    public static final String POSITION_BLACK_LINE = "position";
    public static final String RESET_BLACK_LINE = "reset";
    public static final String DEFAULT_BLACK_LINE = "default";


    public static final int INIT_OMS = 0;
    public static final int TAKE_CUBE_FROM_STAND = 1;
    public static final int PUT_CUBE_FULL = 2;
    public static final int WAREHOUSE_OMS = 3;
    public static final int MIDDLE_CLOSE_GRAB = 4;
    public static final int CLOSE_FULL_GRAB = 5;
    public static final int DOWNLOAD_IN_ROBOT = 6;
    public static final int CLEAN_CUBE = 7;
    public static final int TAKE_CUBE_IN_ROBOT = 8;
    public static final int PUT_CUBE_IN_STAND = 9;
    public static final int OMS_CAMERA_DOWN = 10;
    public static final int OMS_CAMERA_QR = 11;
    public static final int END_SCAN = 12;
    public static final int CLEAN_CUBE_FROM_ROBOT = 13;
    public static final int CUBE_OUT_FROM_SCAN = 14;
    public static final int START_CUBE_OUT_FROM_SCAN = 15;
    public static final int INIT_LIFT_AFTER_CAMERA = 16;

    
    public static IState[][] logStates = new IState[][]
    {
        {
            new InitializeLogic(),

            new Start(),
            new Transition(),
        },
        //FromStartToFirst
        { 

            new Transition(),
        },

        //greenPalateFirst

        { 

            new Transition(),
        },

        //redPalateFirst

        { 

            new Transition(),
        },

        //EndPalateFirst

        { 

            new Transition(),
        },

         //FromFirstToClean

        { 

            new Transition(),
        },

         //FromStartToSecond

        { 

            new Transition(),
        },

         //greenPalateSecond
         
        { 

            new Transition(),
        },

         //redPalateSecond
        { 

            new Transition(),
        },

         //EndPalateSecond

        { 

            new Transition(),
        },

         //FromSecondToClean

        { 

            new Transition(),
        },

        //FromStartToThird

        { 

            new Transition(),
        },

         //greenPalateThird

        { 

            new Transition(),
        },
         
        //redPalateThird

        { 

            new Transition(),
        },
         
        //EndPalateThird

        { 

            new Transition(),
        },

        //FromThirdToClean

        { 

            new Transition(),
        },

         
        //FromStartToFourth
         
        { 

            new Transition(),
        },
        
        //greenPalateFourth
        
        { 

            new Transition(),
        },
        
        //redPalateFourth
        
        { 
            new Transition(),

        },
        
        //EndPalateFourth
        
        { 
            new Transition(),

        },
        
        //FromFourthToClean
        
        { 
            new Transition(),

        },
        
        //Clean
        
        { 
            new Transition(),

        },
        
        //FromStartToWarehouse
        
        { 
            new Transition(),

        },
       
        //FromWarehouseToFirst
        
        { 
            new Transition(),

        },
        
        //FromWarehouseToSecond
        
        { 
            new Transition(),

        },
        
        //FromWarehouseToThird
         
        { 
            new Transition(),

        },
        
        //FromWarehouseToFourth

        { 
            new Transition(),

        },
         
        //Warehouse1
         
        { 
            new Transition(),

        },
        
        //Warehouse2
        
        { 
            new Transition(),

        },
         
        //Warehouse3
         
        { 
            new Transition(),

        },
        
        //Warehouse4

        { 
            new Transition(),

        },
         
        //Warehouse5
         
        { 
            new Transition(),

        },
        
        //CubeOut

        { 
            new OMS(States.TAKE_CUBE_FROM_STAND), 
            new Transition(),
        },
         
        //CubeClean

        { 
            new Sensors(7, 30f, States.USE_RIGHT_SONIC, 0, SmoothEnum.FAST),
            new OMS(States.CLEAN_CUBE),
            new Transition(),
        },
        
        //CubePut
        
        { 
            new OMS(States.PUT_CUBE_IN_STAND),
            new Transition(),
        },

        //CubeIn
        
        { 
            new OMS(States.PUT_CUBE_FULL),
            new Transition(),
        },
        
        //CubeTakeInRobot
        
        { 
            new OMS(States.TAKE_CUBE_IN_ROBOT),
            new Transition(),
        },
         
        //CubeCleanInRobot
         
        { 
            new OMS(States.CLEAN_CUBE_FROM_ROBOT),
            new Transition()

        },
        
        //ToCleanLift
        
        { 
            new OMS(States.CLEAN_CUBE_FROM_ROBOT),
            new Transition()
        },
         
        //END
        
        { 
            new End()
        }
        
    };

    public static IState[][] mainCmodul = new IState[][]{
        { //B1
            new Start(),

            new OMS(States.OMS_CAMERA_DOWN),
            // // new Camera(States.CAMERA_OBJECTS),
            // // new Camera(States.CAMERA_GREEN_CUBE),
            new OMS(States.END_SCAN),
            new Odometry(States.ABSOLUTE_ODOMETRY, 1000, 0, 0, SmoothEnum.DECELERATION),

            new Start(),

            new Odometry(States.ABSOLUTE_ODOMETRY, 1000, 0, 0, SmoothEnum.DECELERATION),

            new Start(),

            new Odometry(States.ABSOLUTE_ODOMETRY, 0, 0, 720, SmoothEnum.DECELERATION),

            new Start(),

            new CmoduleSensors(States.C_SONIC),

            new Start(),

            new CmoduleSensors(States.C_SHARP),

            new Transition(),
        },
        { //B2.1
            new Start(),
            new OMS(States.OMS_CAMERA_DOWN),
            // new Camera(States.CAMERA_YELLOW),
            new Camera(States.CAMERA_BLUE),
            // new Camera(States.CAMERA_WHITE),
            new OMS(States.END_SCAN),

            new Start(),
            new OMS(States.OMS_CAMERA_DOWN),
            new Camera(States.CAMERA_QRC),
            new OMS(States.END_SCAN),
            new Transition(),

        }
    };


}