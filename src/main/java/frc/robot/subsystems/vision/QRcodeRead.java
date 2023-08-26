package frc.robot.subsystems.vision;

import org.opencv.core.Mat;
import org.opencv.objdetect.QRCodeDetector;

import frc.robot.RobotContainer;
import frc.robot.subsystems.JavaCamera;


/**
 * Данный класс предназначен для чтения QR-code
 */
public class QRcodeRead
{
    public JavaCamera server = RobotContainer.server;

    public String read(Mat source)
    {
        String infa = "none";
        QRCodeDetector detector = new QRCodeDetector();
        Mat points = new Mat();
        if (!detector.detectAndDecode(source, points).equals(null)){
            infa = detector.detectAndDecode(source, points);
        }

        return infa;
    }
}