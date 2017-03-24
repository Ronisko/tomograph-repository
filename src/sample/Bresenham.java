package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Bresenham {
    private int d, dx, dy, ai, bi, xi, yi;
    private int x, y;

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

    ArrayList<Pair<Integer,Integer>> bresenhamLine(int x1, int y1, int x2, int y2) {
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

        return points;
    }

}
