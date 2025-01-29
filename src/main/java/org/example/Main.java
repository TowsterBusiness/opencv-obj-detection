package org.example;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        OpenCV.loadLocally();

        System.out.println(Core.getVersionString());

        VideoCapture videoCapture = new VideoCapture(1);

        // Read camera data
        Mat img = new Mat();
        videoCapture.read(img);

        Imgcodecs imageCodecs = new Imgcodecs();
        imageCodecs.imwrite("source.png", img);

        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2HSV);

        imageCodecs.imwrite("hsv.png", img);

        Scalar upper = new Scalar(140, 144, 250);
        Scalar lower = new Scalar(85, 84, 60);

        Mat color = Mat.zeros(300, 300, CvType.CV_8UC3);
        color.setTo(lower);
        Imgproc.cvtColor(color, color, Imgproc.COLOR_HSV2BGR);
        imageCodecs.imwrite("lower.png", color);
        color.setTo(upper);
        Imgproc.cvtColor(color, color, Imgproc.COLOR_HSV2BGR);
        imageCodecs.imwrite("upper.png", color);

        Mat rangeMat = new Mat();
        Core.inRange(img, lower, upper, rangeMat);

        imageCodecs.imwrite("range.png", rangeMat);

        Mat rangeMat = new Mat();
        Core.inRange(img, lower, upper, rangeMat);

        List<MatOfPoint> MOPList = new ArrayList<>();
        Mat contourMat = new Mat();
        Imgproc.findContours(rangeMat, MOPList, contourMat, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Mat coloredRangedMat = new Mat();
        Imgproc.cvtColor(rangeMat, coloredRangedMat, Imgproc.COLOR_GRAY2BGR);
        Imgproc.drawContours(img, MOPList, -1, new Scalar(255, 0, 0), 3);
        imageCodecs.imwrite("contour.png", img);

        double maxArea = -1;
        int maxAreaIdx = 0;
        for (int i = 0; i < MOPList.size(); i++) {
            double area = Imgproc.contourArea(MOPList.get(i));
            if (area > maxArea) {
                maxArea = area;
                maxAreaIdx = i;
            }
        }

        Imgproc.drawContours(img, MOPList, maxAreaIdx, new Scalar(0, 255, 0), 3);

        Imgproc.rectangle(img, Imgproc.boundingRect(MOPList.get(maxAreaIdx)), new Scalar(0, 0, 255), 3);
    }
}