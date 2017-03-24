package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Bresenham {
    private static int sinogramX, sinogramY;
    private static int dx, dy, xi, yi;
    private static double maxBrightness = 0.0;
    private static double minBrightness = 1.0;
    private static Image myImage;
    private static double brightness2;
    private static List<List<Pair>> outputImage = new ArrayList<>();

    public static void setOutputImage(List<List<Pair>> outputImage) {
        Bresenham.outputImage = outputImage;
    }

    public static void setMyImage(Image myImage) {
        Bresenham.myImage = myImage;
    }

    private static void directionX(int a, int b, boolean isX) {
        if (isX) {
            if (a < b) {
                xi = 1;
                dx = b - a;
            } else {
                xi = -1;
                dx = a - b;
            }
        } else {
            if (a < b) {
                yi = 1;
                dy = b - a;
            } else {
                yi = -1;
                dy = a - b;
            }
        }
    }

    public static double normal(int x1, int y1, int x2, int y2) {
        bresenhamLine(x1, y1, x2, y2, false);
        return brightness2;
    }

    public static void inverted(int x1, int y1, int x2, int y2, int sinX, int sinY) {
        sinogramX = sinX;
        sinogramY = sinY;
        bresenhamLine(x1, y1, x2, y2, true);

    }

    private static void bresenhamLine(int x1, int y1, int x2, int y2, boolean isInverted) {
        int d, ai, bi;
        int x, y;
        double brightness = 0.0;
        int size = 1;
        x = x1;
        y = y1;
        directionX(x1, x2, true);
        directionX(y1, y2, false);
        if (isInverted) {
            outputImage.get((int)((x+myImage.getWidth()/2)%myImage.getWidth())).get((int)((y+myImage.getHeight()/2)%myImage.getHeight())).increaseBrightness(getSinogramColor(sinogramX, sinogramY));
        } else {
            brightness = getColor(x, y);
        }
        if (dx > dy)
        {
            ai = (dy - dx) * 2;
            bi = dy * 2;
            d = bi - dx;
            while (x != x2) {
                if (d >= 0) {
                    x += xi;
                    y += yi;
                    d += ai;
                } else {
                    d += bi;
                    x += xi;
                }

                if (isInverted) {
                    outputImage.get((int)((x+myImage.getWidth()/2)%myImage.getWidth())).get((int)((y+myImage.getHeight()/2)%myImage.getHeight())).increaseBrightness(getSinogramColor(sinogramX, sinogramY));

                } else {
                    brightness += getColor(x, y);
                    size++;
                }
            }
        }
        else
        {
            ai = ( dx - dy ) * 2;
            bi = dx * 2;
            d = bi - dy;
            while (y != y2)
            {
                if (d >= 0)
                {
                    x += xi;
                    y += yi;
                    d += ai;
                }
                else
                {
                    d += bi;
                    y += yi;
                }
                if (isInverted) {
                    outputImage.get((int)((x+myImage.getWidth()/2)%myImage.getWidth())).get((int)((y+myImage.getHeight()/2)%myImage.getHeight())).increaseBrightness(getSinogramColor(sinogramX, sinogramY));


                } else {
                    brightness += getColor(x, y);
                    size++;
                }
            }
        }
        if (isInverted) {

        } else {
            if (brightness/size > maxBrightness) {
                maxBrightness = brightness/size;
            }
            if (brightness/size < minBrightness) {
                minBrightness = brightness/size;
            }
        }
        Bresenham.brightness2 = brightness/size;
    }

    private static double getColor(int x, int y) {
        int imageWidth = (int)myImage.getWidth()/2;
        return Controller.getPixelReader().getColor((x+imageWidth)%(int)(myImage.getWidth()), (y+imageWidth)%(int)(myImage.getWidth())).getBrightness();
    }

    private static double getSinogramColor(int x, int y) {
//        System.out.println(x + " " + y);
        int imageWidth = (int)Controller.getWritableImage().getWidth();
        int imageHeight = (int)Controller.getWritableImage().getHeight();
        return Controller.getPixelReader().getColor(((x+imageWidth/2)%(imageWidth)), ((y+imageHeight/2)%imageHeight)).getBrightness();
    }

    public static double getMaxBrightness() {
        return maxBrightness;
    }

    public static double getMinBrightness() {
        return minBrightness;
    }
}
