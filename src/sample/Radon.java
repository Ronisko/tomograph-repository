package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.util.Pair;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Radon {

    private int d, dx, dy, ai, bi, xi, yi;
    private int x, y;
    private ArrayList<ArrayList<Pair<Integer, Integer>>> lines = new ArrayList<>();
    private ArrayList<Double> brightnesses = new ArrayList<>();

    void directionX(int a, int b, boolean isX) {
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

    void bresenhamLine(int x1, int y1, int x2, int y2) {
        ArrayList<Pair<Integer, Integer>> points = new ArrayList<>();
        x = x1;
        y = y1;
        directionX(x1, x2, true);
        directionX(y1, y2, false);
        points.add(new Pair<>(x, y));
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
                points.add(new Pair<>(x, y));
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
                points.add(new Pair<>(x, y));

            }
        }

        lines.add(points);
    }

    public void radonTransform(Image image, int iteration, PixelWriter pixelWriter, ImageView sin, WritableImage wt) {
        double maxBrightness = 0.0;
        double minBrightness = 1.0;
        PixelReader pixelReader = image.getPixelReader();
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        pixelReader.getPixels(0,0, width, height, PixelFormat.getByteBgraInstance(), buffer,0,width * 4);
        int imageWidth = (int)image.getWidth()/2;
        for (int i = 0; i < lines.size(); i++) {
            double brightness = 0;
            for (int j = 0; j < lines.get(i).size(); j++) {
                brightness += pixelReader.getColor((lines.get(i).get(j).getKey()+imageWidth)%(int)(image.getWidth()), (lines.get(i).get(j).getValue()+imageWidth)%(int)(image.getWidth())).getBrightness();
            }
            if (brightness/lines.get(i).size() > maxBrightness) {
                maxBrightness = brightness/lines.get(i).size();
            }
            if (brightness/lines.get(i).size() < minBrightness) {
                minBrightness = brightness/lines.get(i).size();
            }
            brightnesses.add(brightness/lines.get(i).size());
        }

        for (int i = 0; i < lines.size(); i++) {
            pixelWriter.setColor(i, iteration, Color.hsb(0, 0.0, (brightnesses.get(i)-minBrightness)/(maxBrightness-minBrightness)));


        }
    }
}
