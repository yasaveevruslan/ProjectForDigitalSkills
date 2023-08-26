package frc.robot.subsystems.cases;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.RobotContainer;
import frc.robot.subsystems.JavaCamera;
import frc.robot.subsystems.StateMachine;
import frc.robot.subsystems.States;
import frc.robot.subsystems.Training;

/**
 * Данный класс предназначен для установки комманд для работы с камерой
 */
public class Camera implements IState
{
    
    public JavaCamera server = RobotContainer.server;
    public Training train = RobotContainer.train;
    
    private String name;
    private boolean end = false;

    public Camera(String name)
    {
        this.name = name;
    }

    @Override
    public boolean execute()
    {
        RobotContainer.train.setAxisSpeed(0, 0, 0, false);
        if(!StateMachine.isFirst)
        {
            switch(this.name)
            {
                case States.CAMERA_YELLOW:
                end = yellowCube();
                break;

                case States.CAMERA_WHITE:
                end = whiteCube();
                break;

                case States.CAMERA_BLUE:
                end = blueCube();
                break;
                
                case States.CAMERA_QR:
                end = detectorQRCode();
                break;
                
                case States.CAMERA_OBJECTS:
                end = allCube();
                break;
              
                case States.CAMERA_STANDS:
                end = stand();
                break;

                case States.CAMERA_COLOR_STANDS:
                end = colorStands();
                break;
            }
        }
        else 
        {
            this.end = false;
            StateMachine.isFirst = false;
        }
        return end;
    }

    

    private boolean yellowCube()
    {
        server.nowTask = 1;

        if (server.nowResult == 1){
            RobotContainer.train.setGreen(true);
        }else{
            RobotContainer.train.setGreen(false);
        }
        return server.nowResult == 1 && train.getButtonStart() && Timer.getFPGATimestamp() - StateMachine.StartTime > 0.5f;

    }

    private boolean blueCube()
    {
        server.nowTask = 2;

        if (server.nowResult == 1){
            RobotContainer.train.setGreen(true);
        }else{
            RobotContainer.train.setGreen(false);
        }
        return server.nowResult == 1 && train.getButtonStart() && Timer.getFPGATimestamp() - StateMachine.StartTime > 0.5f;
    

    }

    private boolean whiteCube()
    {
        server.nowTask = 3;

        if (server.nowResult == 1){
            RobotContainer.train.setGreen(true);
        }else{
            RobotContainer.train.setGreen(false);
        }
        
        return server.nowResult == 1 && train.getButtonStart()  && Timer.getFPGATimestamp() - StateMachine.StartTime > 0.5f;
    }

    private boolean allCube(){
        server.nowTask = 6;

        boolean object = server.nowResult == 1 || server.nowResult == 2 || server.nowResult == 3;

        if (object){
            RobotContainer.train.setGreen(true);
        }else{
            RobotContainer.train.setGreen(false);
        }

        return object && train.getButtonStart()  && Timer.getFPGATimestamp() - StateMachine.StartTime > 0.5f;
    }




    private boolean detectorQRCode()
    {
        server.nowTask = 4;
            if (server.colorCube != null) 
            {
                SmartDashboard.putString("QR_F", server.colorCube);
            }
            else
            {
                SmartDashboard.putString("QR_F", "none");
            }
        
        return RobotContainer.train.getButtonStart() && Timer.getFPGATimestamp() - StateMachine.StartTime > 1f;
    }



    private boolean colorStands()
    {
        server.nowTask = 8;

        if (server.colorStand.equals("red")){
            SmartDashboard.putString("StandF", "red" );
        }else if(server.colorStand.equals("green")){
            SmartDashboard.putString("StandF", "green" );
        }else{
            SmartDashboard.putString("StandF", "none" );
        }
        return Timer.getFPGATimestamp() - StateMachine.StartTime > 0.4f && RobotContainer.train.getButtonStart();
    }

    private boolean stand()
    {
        server.nowTask = 7;

        if(RobotContainer.server.alignCamera){
            RobotContainer.train.setAxisSpeed(0, 0, 0, false);
            SmartDashboard.putBoolean("alignCamera", true);
            return true;
        }else{
            RobotContainer.train.setAxisSpeed(0, 32, 0, false);
            SmartDashboard.putBoolean("alignCamera", false);
            return false;
        }

    }


} 