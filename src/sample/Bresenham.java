package sample;

import javafx.scene.image.Image;

public class Bresenham {
    private static int dx, dy, xi, yi;
    private static double maxBrightness = 0.0;
    private static double minBrightness = 1.0;
    private static Image myImage;

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

    public static double bresenhamLine(int x1, int y1, int x2, int y2) {
        int d, ai, bi;
        int x, y;
        double brightness;
        int size = 1;
        x = x1;
        y = y1;
        directionX(x1, x2, true);
        directionX(y1, y2, false);
        brightness = getColor(x, y);
        if (dx > dy)
        {
            ai = (dy - dx) * 2;
            bi = dy * 2;
            d = bi - dx;
            while (x != x2)
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
                    x += xi;
                }
                brightness += getColor(x, y);
                size++;
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
                brightness += getColor(x, y);
                size++;
            }
        }
        if (brightness/size > maxBrightness) {
            maxBrightness = brightness/size;
        }
        if (brightness/size < minBrightness) {
            minBrightness = brightness/size;
        }
        return brightness/size;
    }

    private static double getColor(int x, int y) {
        int imageWidth = (int)myImage.getWidth()/2;
        return Controller.getPixelReader().getColor((x+imageWidth)%(int)(myImage.getWidth()), (y+imageWidth)%(int)(myImage.getWidth())).getBrightness();
    }

    public static double getMaxBrightness() {
        return maxBrightness;
    }

    public static double getMinBrightness() {
        return minBrightness;
    }
}
