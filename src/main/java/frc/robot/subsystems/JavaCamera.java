package frc.robot.subsystems;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.vision.ColorCube;
import frc.robot.subsystems.vision.QRcodeRead;

/**
 * Данный класс предназначен для запуска камеры и выполнения команд связанных со зрением
 */
public class JavaCamera
{

    public ColorCube cube = new ColorCube();
    public QRcodeRead detectorQR = new QRcodeRead();

    private UsbCamera camera;
    private CvSink cvSink;
    public CvSource blue;
    public CvSource white;
    public CvSource yellow;
    public CvSource qr;
    public CvSource countrCube;
    public CvSource greenStand;
    public CvSource redStand;

    public int nowTask = 0;
    public int nowResult = 0; 
    public int nowIteration = 0;
    public String colorCube = "none", colorStand = "none";
    public boolean alignCamera = false;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    
    public JavaCamera()
    {

        camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(640, 480);
        camera.setFPS(130);

        cvSink = CameraServer.getInstance().getVideo();

        blue = CameraServer.getInstance().putVideo("blue", 640, 480);
        white = CameraServer.getInstance().putVideo("white", 640, 480);
        yellow = CameraServer.getInstance().putVideo("yellow", 640, 480);
        qr = CameraServer.getInstance().putVideo("QR_Code", 640, 480);
        countrCube = CameraServer.getInstance().putVideo("contours", 640, 480);
        
        greenStand = CameraServer.getInstance().putVideo("greenStand", 640, 480);
        redStand = CameraServer.getInstance().putVideo("redStand", 640, 480);


    }

    /**
     * Запуска потока работы камеры
     */

    public void Start()
    {
        new Thread(() -> {
            Mat source = new Mat();
            while (true)
            {
                try
                {
                    if (cvSink.grabFrame(source) == 0) 
                        continue;

                    if (source.cols() < 640 || source.rows() < 480)
                        continue;

                    if (nowTask == 1)
                        nowResult = cube.yellowCube(source);

                    if (nowTask == 2) 
                        nowResult = cube.blueCube(source);
                    
                    if (nowTask == 3)
                        nowResult = cube.whiteCube(source);
                    
                    if (nowTask == 4)
                        colorCube = detectorQR.read(source);

                    if(nowTask == 5)
                        nowResult = cube.greenCube(source);

                    if(nowTask == 6)
                        nowResult = cube.objects(source);

                    if(nowTask == 7)
                        alignCamera = cube.Stand(source);

                    if(nowTask == 8)
                        colorStand = cube.colorStand(source);


                    SmartDashboard.putNumber("ResultCamera", nowResult);
                    SmartDashboard.putString("ResultCameraString", colorCube);
                    SmartDashboard.putString("ResultCameraStand", colorStand);

                    Thread.sleep(5);
                } catch (Exception e) {
                }
            }
        }).start();
    }


}