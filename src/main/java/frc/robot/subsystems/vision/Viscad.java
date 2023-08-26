package frc.robot.subsystems.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * Данный класс предназначен для методов, которые выполняют обработку картинки с камеры
 */
public class Viscad {
    
    public static Mat RotateImage(Mat src, double deg) {
        Mat dst = new Mat(src.rows(), src.cols(), src.type());
        Point src_center = new Point(src.cols() / 2.0F, src.rows() / 2.0F);
        Mat rot_mat = Imgproc.getRotationMatrix2D(src_center, 360 - deg, 1.0);
        Imgproc.warpAffine(src, dst, rot_mat, src.size());

        rot_mat.release();

        return dst;
    }

    public static Mat Threshold(Mat src, Point red, Point green, Point blue) {
        Mat outImage = new Mat();
        Core.inRange(src, new Scalar(red.x, green.x, blue.x), new Scalar(red.y, green.y, blue.y), outImage);
        
        return outImage;
    }

    public static Mat ThresholdGray(Mat src, Point range) {
        Mat outImage = new Mat();
        Core.inRange(src, new Scalar(range.x), new Scalar(range.y), outImage);

        return outImage;
    }

    public static Mat BinaryAnd(Mat first, Mat second) {
        Mat dst = new Mat();
        Core.bitwise_and(first, second, dst);
        return dst;
    }

    public static Mat BinaryOr(Mat first, Mat second) {
        Mat dst = new Mat();
        Core.bitwise_or(first, second, dst);
        return dst;
    }

    public static Mat BinaryNot(Mat src) {
        Mat dst = new Mat();
        Core.bitwise_not(src, dst);
        return dst;
    }

    public static Mat ResizeImage(Mat src, int w, int h) {
        Mat resizeImage = new Mat();
        Size sz = new Size(w, h);
        Imgproc.resize(src, resizeImage, sz);
        return resizeImage;
    }

    public static Mat ExtractImage(Mat src, Rect rect)
    {
        return src.submat(rect);
    }

    public static Mat Blur(Mat src, int power)
    {
        Mat dst = new Mat();
        Imgproc.blur(src, dst, new Size(power, power));
        return dst;
    }

    public static Mat Dilate(Mat src, int power)
    {
        Mat dst2 = new Mat();
        src.copyTo(dst2);

        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                        new Size(2 * power + 1, 2 * power + 1));
        Imgproc.dilate(src, dst2, element1);
        element1.release();

        return dst2;
    }

    public static int ImageTrueArea(Mat src)
    {
        return Core.countNonZero(src);
    }

    public static List<MatOfPoint> findContours(Mat image){
        List<MatOfPoint> contours = new ArrayList<>();
        Mat imageContours = new Mat();

        Imgproc.findContours(image, contours, imageContours, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        return contours;
    }

    public static Point CenterOfMass(Mat src) {
        Moments moments = Imgproc.moments(src);
        return new Point(moments.m10 / moments.m00, moments.m01 / moments.m00);
    }

    // public static Mat AutoBrightnessCAD(Mat cut, Mat needed, int value, boolean outBGR)
    // {
    //     Mat hsvCut = new Mat();
    //     Imgproc.cvtColor(cut, hsvCut, Imgproc.COLOR_BGR2HSV);
    //     ArrayList<Mat> dst = new ArrayList<>(3);
    //     Core.split(hsvCut, dst);

    //     MatOfDouble mu = new MatOfDouble();
    //     MatOfDouble sigma = new MatOfDouble();
    //     Core.meanStdDev(dst.get(2), mu, sigma);
    //     float mulK = (float)value / (float)mu.get(0,0)[0];

    //     Mat hsvOut = new Mat();
    //     Imgproc.cvtColor(needed, hsvOut, Imgproc.COLOR_BGR2HSV);
    //     ArrayList<Mat> dstOut = new ArrayList<>(3);
    //     Core.split(hsvOut, dstOut);
    //     Mat bright = new Mat();
    //     Core.multiply(dstOut.get(2), new Scalar(mulK), bright);

    //     Mat out = new Mat();
    //     Core.merge(new ArrayList<Mat>(Arrays.asList(new Mat[]{dstOut.get(0), dstOut.get(1), bright})), out);
    //     if (outBGR)
    //     {
    //         Mat newOut = new Mat();
    //         Imgproc.cvtColor(out, newOut, Imgproc.COLOR_HSV2BGR);
    //         return newOut;
    //     }
    //     return out;
    // }

    public static Mat RejectBordersWOBottomFast(Mat src, int space) {        
        // Mat output = Mat.zeros(src.rows() + 2, src.cols() + 2, CvType.CV_8U);
        Mat output = new Mat();        
        src.copyTo(output);
        // output = ResizeImage(output, src.cols() + 2, src.rows() + 2);        
        int h = src.rows();
        int w = src.cols();
        // int size = (int) (src.total() * src.channels());        
        // double[] temp = new double[size];
        // Imgproc.floodFill(output, new Mat(), new Point(0, 0), new Scalar(0));
        for (int i = 0; i + space < h; i += space) {
            double[] temp = output.get(i, 0);            if (temp[0] == 255) {
                Imgproc.floodFill(output, new Mat(), new Point(0, i), new Scalar(0));            }
            temp = output.get(i, w - 1);            if (temp[0] == 255) {
                Imgproc.floodFill(output, new Mat(), new Point(w - 1, i), new Scalar(0));            }
        }        for (int i = 0; i + space < w; i += space) {
            double[] temp = output.get(0, i);            if (temp[0] == 255) {
                Imgproc.floodFill(output, new Mat(), new Point(i, 0), new Scalar(0));            }
        }        return output;
    }
}