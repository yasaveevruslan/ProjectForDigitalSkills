package frc.robot.subsystems.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Function;

/**
 * Данный класс предназначен для выполнения обработки и вывода результата обработки картинки с камеры
 */
public class ColorCube
{
   
    /**
     * Метод предназначен для нахождения желтого цвета/куба и вывод результата поиска
     * Если выводит 1, то цвет/куб найден
     * Если выводит 0, то цвет/куб не найден
     */
    public int yellowCube(Mat src)
    {
        Scalar yellow_low = new Scalar(20, 80, 80);
        Scalar yellow_upper = new Scalar(45, 255, 255);

        Mat source = new Mat();
        Imgproc.cvtColor(src, source, Imgproc.COLOR_BGR2HSV);
        src.release();

        Mat result = new Mat();
        Core.inRange(source, yellow_low, yellow_upper, result);
        source.release();
        Mat thresholdYellow = new Mat();

        Imgproc.threshold(result, thresholdYellow, 40, 50, Imgproc.THRESH_TOZERO);
        result.release();

        Mat blurredY = Viscad.Blur(thresholdYellow, 1);// 3
        Mat dilatedY = Viscad.Dilate(blurredY, 3);
        blurredY.release();

        RobotContainer.server.yellow.putFrame(dilatedY);
        int counted = Viscad.ImageTrueArea(dilatedY);
        SmartDashboard.putNumber("countZero", counted);
        dilatedY.release();


        SmartDashboard.putBoolean("BooleanYellow", counted > 18000);


        if (counted > 18000)
            return 1;
        else
            return 0;
    }

    /**
     * Метод предназначен для нахождения красного цвета/ белого куба и вывод результата поиска
     * Если выводит 1, то цвет/куб найден
     * Если выводит 0, то цвет/куб не найден
     */

    public int whiteCube(Mat src)
    {
        Scalar blue_low = new Scalar(100, 100, 100);
        Scalar blue_upper = new Scalar(130, 255, 255);

        Scalar white_low = new Scalar(0, 150, 150);
        Scalar white_upper = new Scalar(10, 255, 255);

        Mat source = new Mat();
        Imgproc.cvtColor(src, source, Imgproc.COLOR_BGR2HSV);

        Mat Cblue = new Mat();
        Mat CWhite = new Mat();


        Core.inRange(source, blue_low, blue_upper, Cblue);
        Core.inRange(source, white_low, white_upper, CWhite);
             
        source.release();

        Mat thresholdRed = new Mat();
        Mat thresholdBlue = new Mat();

        Imgproc.threshold(CWhite, thresholdRed, 80, 100, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(Cblue, thresholdBlue, 100, 120, Imgproc.THRESH_TOZERO);

        CWhite.release();
        Cblue.release();


        Mat blurredR = Viscad.Blur(thresholdRed, 1);// 3
        Mat dilatedR = Viscad.Dilate(blurredR, 6);// 7
        blurredR.release();
    
        Mat blurredB = Viscad.Blur(thresholdBlue, 1);// 3
        Mat dilatedB = Viscad.Dilate(blurredB, 1);
        blurredB.release();

        dilatedR = Viscad.RejectBordersWOBottomFast(dilatedR, 5);
        RobotContainer.server.blue.putFrame(dilatedB);
        RobotContainer.server.white.putFrame(dilatedR);


        int countW = Core.countNonZero(dilatedR);
        int countB = Core.countNonZero(dilatedB);

        SmartDashboard.putNumber("White", countW);
        SmartDashboard.putNumber("Blue", countB);

        SmartDashboard.putBoolean("BooleanWhite", countW > 6000);
        SmartDashboard.putBoolean("BooleanBlue", countB > 10000);

        dilatedR.release(); 
        dilatedB.release(); 


        if (countB > 15000)
        {
            return 0;
        }
        else
        {
            if (countW > 25000)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
        }
    }

    /**
     * Метод предназначен для нахождения синего цвета/куба и вывод результата поиска
     * Если выводит 1, то цвет/куб найден
     * Если выводит 0, то цвет/куб не найден
     */

    public int blueCube(Mat src)
    {

        Scalar blue_low = new Scalar(100, 100, 100);
        Scalar blue_upper = new Scalar(130, 255, 255);


        Mat source = new Mat();
        Imgproc.cvtColor(src, source, Imgproc.COLOR_BGR2HSV);
        src.release();

        Mat Cblue = new Mat();
        Core.inRange(source, blue_low, blue_upper, Cblue);
        source.release();

        RobotContainer.server.blue.putFrame(Cblue);
 
        int countB = Core.countNonZero(Cblue);
        SmartDashboard.putNumber("Blue", countB);
        SmartDashboard.putBoolean("BooleanBlue", countB > 15000);
        Cblue.release();
 

        if (countB > 20000) {
            return 1; 
        } else {
            return 0;
        }
    }

    /**
     * Метод предназначен для нахождения синего и красного цвета/ синего и белого куба и вывод результата поиска
     * Если выводит 2, то синий цвет/куб найден
     * Если выводит 1, то красный цвет/ белый куб найден
     * Если выводит 0, то цвета/кубы не найдены
     */

    public int greenCube(Mat src){
      
        Scalar blue_low = new Scalar(100, 100, 100);
        Scalar blue_upper = new Scalar(130, 255, 255);

        Scalar white_low = new Scalar(0, 155, 100);
        Scalar white_upper = new Scalar(15, 255, 255);


        Mat source = new Mat();
        Imgproc.cvtColor(src, source, Imgproc.COLOR_BGR2HSV);
        src.release();

        Mat Cblue = new Mat();
        Mat CWhite = new Mat();

        Core.inRange(source, blue_low, blue_upper, Cblue);
        Core.inRange(source, white_low, white_upper, CWhite);
        source.release();

        Mat thresholdRed = new Mat();

        Imgproc.threshold(CWhite, thresholdRed, 0, 15, Imgproc.THRESH_TOZERO);


        CWhite.release();

        Mat blurredR = Viscad.Blur(thresholdRed, 1);// 3
        Mat dilatedR = Viscad.Dilate(blurredR, 3);// 7
        blurredR.release();

        RobotContainer.server.blue.putFrame(Cblue);
        RobotContainer.server.white.putFrame(dilatedR);

        int countW = Core.countNonZero(dilatedR);
        int countB = Core.countNonZero(Cblue);
        SmartDashboard.putNumber("White", countW);
        SmartDashboard.putNumber("Blue", countB);
        SmartDashboard.putBoolean("BooleanWhite", countW > 20000);
        SmartDashboard.putBoolean("BooleanBlue", countB > 15000);
        Cblue.release();
        blurredR.release(); 

        if (countB > 13000) {
            return 1; 
        } else {
            if (countW > 13000) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    /**
     * Метод предназначен для нахождения синего, красного и жёлтого цвета/ синего, белого и жёлтого куба и вывод результата поиска
     * Если выводит 3, то красный цвет/ белый куб найден
     * Если выводит 2, то жёлтый цвет/куб найден
     * Если выводит 1, то синий цвет/ белый куб найден
     * Если выводит 0, то цвета/кубы не найдены
     */

    public int objects(Mat src)
    {
        Scalar yellow_low = new Scalar(20, 80, 80);
        Scalar yellow_upper = new Scalar(45, 255, 255);


        Scalar blue_low = new Scalar(100, 100, 100);
        Scalar blue_upper = new Scalar(130, 255, 255);

        Scalar white_low = new Scalar(0, 150, 150);
        Scalar white_upper = new Scalar(10, 255, 255);

        Mat source = new Mat();
        Imgproc.cvtColor(src, source, Imgproc.COLOR_BGR2HSV);

        Mat Cblue = new Mat();
        Mat CWhite = new Mat();
        Mat CYellow = new Mat();

        Core.inRange(source, blue_low, blue_upper, Cblue);
        Core.inRange(source, white_low, white_upper, CWhite);
        Core.inRange(source, yellow_low, yellow_upper, CYellow);                    
        source.release();

        Mat thresholdRed = new Mat();
        Mat thresholdBlue = new Mat();
        Mat thresholdYellow = new Mat();

        Imgproc.threshold(CWhite, thresholdRed, 80, 100, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(CYellow, thresholdYellow, 40, 50, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(Cblue, thresholdBlue, 100, 120, Imgproc.THRESH_TOZERO);

        CWhite.release();
        CYellow.release();
        Cblue.release();


        Mat blurredR = Viscad.Blur(thresholdRed, 1);// 3
        Mat dilatedR = Viscad.Dilate(blurredR, 6);// 7
        blurredR.release();
    
        Mat blurredB = Viscad.Blur(thresholdBlue, 1);// 3
        Mat dilatedB = Viscad.Dilate(blurredB, 1);
        blurredB.release();

        Mat blurredY = Viscad.Blur(thresholdYellow, 1);// 3
        Mat dilatedY = Viscad.Dilate(blurredY, 3);
        blurredY.release();

        dilatedR = Viscad.RejectBordersWOBottomFast(dilatedR, 5);
        RobotContainer.server.blue.putFrame(dilatedB);
        RobotContainer.server.white.putFrame(dilatedR);
        RobotContainer.server.yellow.putFrame(dilatedY);

        int countW = Core.countNonZero(dilatedR);
        int countB = Core.countNonZero(dilatedB);
        int countY = Core.countNonZero(dilatedY);

        SmartDashboard.putNumber("White", countW);
        SmartDashboard.putNumber("Blue", countB);
        SmartDashboard.putNumber("Yellow", countY);

        SmartDashboard.putBoolean("BooleanWhite", countW > 6000);
        SmartDashboard.putBoolean("BooleanBlue", countB > 10000);
        SmartDashboard.putBoolean("BooleanYellow", countY > 10000);

     
        dilatedR.release(); 
        dilatedB.release(); 
        dilatedY.release(); 

        if (countB > 15000)
        {
            return 1;
        }
        else
        {
            if(countY > 10000)
            {
                return 2;
            }
            else
            {
                if (countW > 25000)
                {
                    return 3;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    /**
     * Метод предназначен для нахождения синего, красного и жёлтого цвета/ синего, белого и жёлтого куба по контуру и вывод результата поиска
     * Если выводит 3, то красный цвет/ белый куб найден
     * Если выводит 2, то жёлтый цвет/куб найден
     * Если выводит 1, то синий цвет/ белый куб найден
     * Если выводит 0, то цвета/кубы не найдены
     */

    public int objectsContour(Mat src)
    {
        Mat img = src.clone();
        Scalar yellow_low = new Scalar(20, 100, 100);
        Scalar yellow_upper = new Scalar(28, 255, 255);

        Scalar blue_low = new Scalar(100, 100, 100);
        Scalar blue_upper = new Scalar(130, 255, 255);

        Scalar white_low = new Scalar(0, 150, 150);
        Scalar white_upper = new Scalar(15, 255, 255);


        Mat source = new Mat();
        Imgproc.cvtColor(src, source, Imgproc.COLOR_BGR2HSV);

        Mat Cblue = new Mat();
        Mat CWhite = new Mat();
        Mat CYellow = new Mat();

        Core.inRange(source, blue_low, blue_upper, Cblue);
        Core.inRange(source, white_low, white_upper, CWhite);
        Core.inRange(source, yellow_low, yellow_upper, CYellow);                    
        source.release();

        Mat thresholdRed = new Mat();
        Mat thresholdBlue = new Mat();
        Mat thresholdYellow = new Mat();

        Imgproc.threshold(CWhite, thresholdRed, 0, 255, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(CYellow, thresholdYellow, 30, 45, Imgproc.THRESH_TOZERO);
        Imgproc.threshold(Cblue, thresholdBlue, 100, 120, Imgproc.THRESH_TOZERO);

        CWhite.release();
        CYellow.release();
        Cblue.release();


        Mat blurredR = Viscad.Blur(thresholdRed, 1);// 3
        Mat dilatedR = Viscad.Dilate(blurredR, 3);// 7
        blurredR.release();
    
        Mat blurredB = Viscad.Blur(thresholdBlue, 1);// 3
        Mat dilatedB = Viscad.Dilate(blurredB, 1);
        blurredB.release();

        Mat blurredY = Viscad.Blur(thresholdYellow, 1);// 3
        Mat dilatedY = Viscad.Dilate(blurredY, 5);
        blurredY.release();

        List<MatOfPoint> bluePoints = new ArrayList<>();
        List<MatOfPoint> whitePoints = new ArrayList<>();
        List<MatOfPoint> yellowPoints = new ArrayList<>();


        Imgproc.findContours(dilatedR, whitePoints, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        SmartDashboard.putNumber("SizeContoursWhite", whitePoints.size());
        Imgproc.drawContours(img, whitePoints, -1, new Scalar(0,255,255),2, Imgproc.LINE_AA);

        Imgproc.findContours(dilatedB, bluePoints, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        SmartDashboard.putNumber("SizeContoursBlue", bluePoints.size());
        Imgproc.drawContours(img, bluePoints, -1, new Scalar(255,0,0),2, Imgproc.LINE_AA);

        Mat hierarchey = new Mat();

        Imgproc.findContours(dilatedY, yellowPoints, hierarchey, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        SmartDashboard.putNumber("SizeContoursYellow", yellowPoints.size());
        Imgproc.drawContours(img, yellowPoints, -1, new Scalar(255,255,255),2, Imgproc.LINE_AA);


        RobotContainer.server.blue.putFrame(dilatedB);
        RobotContainer.server.white.putFrame(dilatedR);
        RobotContainer.server.yellow.putFrame(dilatedY);

        RobotContainer.server.countrCube.putFrame(img);

        int countW = Core.countNonZero(dilatedR);
        int countB = Core.countNonZero(dilatedB);
        int countY = Core.countNonZero(dilatedY);

        SmartDashboard.putNumber("White", countW);
        SmartDashboard.putNumber("Blue", countB);
        SmartDashboard.putNumber("Yellow", countY);

        SmartDashboard.putBoolean("BooleanWhite", countW > 10000);
        SmartDashboard.putBoolean("BooleanBlue", countB > 10000);
        SmartDashboard.putBoolean("BooleanYellow", countY > 10000);

     
        dilatedR.release(); 
        dilatedB.release(); 
        dilatedY.release(); 

        if (countB > 8000)
        {
            return 1;
        }
        else
        {
            if(countY > 10000)
            {
                return 2;
            }
            else
            {
                if (countW > 6000)
                {
                    return 3;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    /**
     * Метод предназначен для нахождения зелёного и красного цвета/ зелёного и красного стенда для кубов и вывод результата поиска
     * Используеться для езды
     * Если стенд найден по середине камеры, то метод выдает TRUE
     * Если стенд не найден по середине камеры, то метод выдает FALSE
     */

    public boolean Stand(Mat src) {
        int width = src.cols();
        int cropHeight = 69;

        Rect rectCrop = new Rect(0, 300, width, cropHeight);
        Mat sourse = new Mat(src, rectCrop);

        RobotContainer.server.countrCube.putFrame(sourse);

        SmartDashboard.putNumber("Height", sourse.height());
        SmartDashboard.putNumber("Width", sourse.width());

        Mat hsv = new Mat();
        Imgproc.cvtColor(sourse, hsv, Imgproc.COLOR_BGR2HSV);
        sourse.release();

        Mat threshRedStand = Viscad.Threshold(hsv, new Point(100, 200), new Point(130, 255), new Point(129, 255));
        Mat threshGreenStand = Viscad.Threshold(hsv, new Point(50, 100), new Point(50, 255), new Point(50, 255));

        Mat blurredR = Viscad.Blur(threshRedStand, 3);// 3
        Mat dilatedR = Viscad.Dilate(blurredR, 4);// 7
        blurredR.release();

        RobotContainer.server.redStand.putFrame(dilatedR);
        RobotContainer.server.greenStand.putFrame(threshGreenStand);

        int redStandArea = Core.countNonZero(dilatedR);
        int greenStandArea = Core.countNonZero(threshGreenStand);

        SmartDashboard.putNumber("redStandArea", redStandArea);
        SmartDashboard.putNumber("greenStandArea", greenStandArea);

        Point centerRedArea = Viscad.CenterOfMass(dilatedR);
        double doubleRedArea = centerRedArea.x;

        Point centerGreenArea = Viscad.CenterOfMass(threshGreenStand);
        double doubleGreenArea = centerGreenArea.x;

        SmartDashboard.putNumber("greenStandPointX", doubleGreenArea);
        SmartDashboard.putNumber("redStandPointX", doubleRedArea);

        SmartDashboard.putNumber("Razniza", (float)doubleRedArea - 320);
        hsv.release();
        threshRedStand.release();
        dilatedR.release();
        blurredR.release();
        threshGreenStand.release();

        if(Function.InRangeBool(Math.abs((float)doubleRedArea - 320),  -9f, 9f) || Function.InRangeBool(Math.abs((float)doubleGreenArea - 320),  -5f, -5f)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Метод предназначен для нахождения зелёного и красного цвета/ зелёного и красного стенда для кубов и вывод результата поиска
     * Если стенд найден, то метод выдает цвет стенда
     * Если стенд не найден, то метод выдает none
     */

    public String colorStand(Mat src) {

        int width = src.cols()-180;
        int cropHeight = src.rows();

        Rect rectCrop = new Rect(0, 0, width, cropHeight);
        Mat sourse = new Mat(src, rectCrop);

        RobotContainer.server.countrCube.putFrame(sourse);

        SmartDashboard.putNumber("Height", sourse.height());
        SmartDashboard.putNumber("Width", sourse.width());

        Mat hsv = new Mat();
        Imgproc.cvtColor(sourse, hsv, Imgproc.COLOR_BGR2HSV);
        sourse.release();

        Mat threshRedStand = Viscad.Threshold(hsv, new Point(170, 255), new Point(160, 255), new Point(140, 255));
        Mat threshGreenStand = Viscad.Threshold(hsv, new Point(50, 100), new Point(50, 255), new Point(50, 255));

        Mat blurredR = Viscad.Blur(threshRedStand, 2);// 3
        Mat dilatedR = Viscad.Dilate(blurredR, 2);// 7
        blurredR.release();

        RobotContainer.server.redStand.putFrame(dilatedR);
        RobotContainer.server.greenStand.putFrame(threshGreenStand);

        int redStandArea = Core.countNonZero(dilatedR);
        int greenStandArea = Core.countNonZero(threshGreenStand);

        SmartDashboard.putNumber("redStandArea", redStandArea);
        SmartDashboard.putNumber("greenStandArea", greenStandArea);
        
        hsv.release();
        threshRedStand.release();
        dilatedR.release();
        blurredR.release();
        threshGreenStand.release();

        if(redStandArea > 20000){
            return "red";
        }else if(greenStandArea > 28000){
            return "green";
        }else{
            return "none";
        }

    }
}