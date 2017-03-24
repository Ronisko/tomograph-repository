package sample;

import javafx.scene.image.*;
import javafx.util.Pair;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Radon {

    private static Emitter emitter;
    private static ArrayList<Detector> detectors;

    private static List<List<ArrayList<Pair<Integer, Integer>>>> allLines = new ArrayList<>();
    private static ArrayList<ArrayList<Double>> brightnesseses = new ArrayList<>();


    public static void radonTransform(Image image, PixelWriter pixelWriter, ImageView sin, WritableImage wt) {
        long start = System.nanoTime();
        detectors = new ArrayList<>();

        emitter = new Emitter();
        for (int i = 0; i < Input.getDetectorsNumber(); i++) {
            detectors.add(new Detector());
        }
        Bresenham bresenham = new Bresenham();

        for (int i = 0; i < Input.getEmittersNumber(); i++) {
            emitter.setAll(Math.floor(Input.getR()*Math.cos(i*Input.getAlfa())), Math.floor(Input.getR()*Math.sin(i*Input.getAlfa())));
            List<ArrayList<Pair<Integer, Integer>>> lines = new ArrayList<>();
            for (int j = 0; j < Input.getDetectorsNumber(); j++) {
                detectors.get(j).setAll(
                        Input.getR() * Math.cos(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))),
                        Input.getR() * Math.sin(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))));
                lines.add(bresenham.bresenhamLine((int)emitter.getX(), (int)emitter.getY(), (int)detectors.get(j).getX(), (int)detectors.get(j).getY()));
            }
            allLines.add(lines);
        }



        double maxBrightness = 0.0;
        double minBrightness = 1.0;
        PixelReader pixelReader = image.getPixelReader();
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        pixelReader.getPixels(0,0, width, height, PixelFormat.getByteBgraInstance(), buffer,0,width * 4);
        int imageWidth = (int)image.getWidth()/2;
        for (int k = 0; k < allLines.size(); k++) {
            ArrayList<Double> brightnesses = new ArrayList<>();
            for (int i = 0; i < allLines.get(k).size(); i++) {
                double brightness = 0;
                for (int j = 0; j < allLines.get(k).get(i).size(); j++) {
                    brightness += pixelReader.getColor((allLines.get(k).get(i).get(j).getKey()+imageWidth)%(int)(image.getWidth()), (allLines.get(k).get(i).get(j).getValue()+imageWidth)%(int)(image.getWidth())).getBrightness();
                }
                if (brightness/allLines.get(k).get(i).size() > maxBrightness) {
                    maxBrightness = brightness/allLines.get(k).get(i).size();
                }
                if (brightness/allLines.get(k).get(i).size() < minBrightness) {
                    minBrightness = brightness/allLines.get(k).get(i).size();
                }
//                System.out.print(allLines.get(k).get(i).size()+ " ");
                brightnesses.add(brightness/allLines.get(k).get(i).size());
            }
//            System.out.println();
            brightnesseses.add(brightnesses);
        }

        for (int k = 0; k < Input.getEmittersNumber(); k++) {
            for (int i = 0; i < Input.getDetectorsNumber(); i++) {
                pixelWriter.setColor(k, i, Color.hsb(0, 0.0, (brightnesseses.get(k).get(i)-minBrightness)/(maxBrightness-minBrightness)));
            }
        }
        sin.setImage(wt);

        long elapsedTime = System.nanoTime() - start;
        System.out.println((double)elapsedTime/1000000000);
    }

    public void inverseRadonTransform() {

    }

}
