package sample;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Radon {

    private static Emitter emitter;
    private static List<Detector> detectors;

    private static List<List<Double>> brightnesseses = new ArrayList<>();


    public static void radonTransform(PixelWriter pixelWriter, ImageView sin, WritableImage wt) {
        detectors = new ArrayList<>();

        emitter = new Emitter();
        for (int i = 0; i < Input.getDetectorsNumber(); i++) {
            detectors.add(new Detector());
        }

        Bresenham bresenham = new Bresenham();

        for (int i = 0; i < Input.getEmittersNumber(); i++) {
            emitter.setAll(Math.floor(Input.getR()*Math.cos(i*Input.getAlfa())), Math.floor(Input.getR()*Math.sin(i*Input.getAlfa())));
            List<Double> lines = new ArrayList<>();
            for (int j = 0; j < Input.getDetectorsNumber(); j++) {
                detectors.get(j).setAll(
                        Input.getR() * Math.cos(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))),
                        Input.getR() * Math.sin(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))));
                lines.add(bresenham.normal((int)emitter.getX(), (int)emitter.getY(), (int)detectors.get(j).getX(), (int)detectors.get(j).getY()));
            }
            brightnesseses.add(lines);
        }

        for (int i = 0; i < Input.getEmittersNumber(); i++) {
            for (int j = 0; j < Input.getDetectorsNumber(); j++) {
                pixelWriter.setColor(j, i, Color.hsb(0, 0.0, ((brightnesseses.get(i).get(j)-Bresenham.getMinBrightness())/(Bresenham.getMaxBrightness()-Bresenham.getMinBrightness()))));
            }
        }
        sin.setImage(wt);

    }

    private static List<List<Pair>> outputImage = new ArrayList<>();

    public static void inverseRadonTransform(PixelWriter pixelWriter, ImageView sin, WritableImage wt, int range) {
        Bresenham.setOutputImage(outputImage);
        for (int i = 0; i < range; i++) {
            List<Pair> list = new ArrayList<>();
            for (int j = 0; j < range; j++) {
                list.add(new Pair());
            }
            outputImage.add(list);
        }

        for (int i = 0; i < Input.getEmittersNumber(); i++) {
            emitter.setAll(Math.floor(Input.getR()*Math.cos(i*Input.getAlfa())), Math.floor(Input.getR()*Math.sin(i*Input.getAlfa())));
            for (int j = 0; j < Input.getDetectorsNumber(); j++) {
                detectors.get(j).setAll(
                        Input.getR() * Math.cos(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))),
                        Input.getR() * Math.sin(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))));
                Bresenham.inverted((int)detectors.get(j).getX(), (int)detectors.get(j).getY(), (int)emitter.getX(), (int)emitter.getY(), i, j);
            }
        }

        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                pixelWriter.setColor(j, i, Color.hsb(0, 0.0, outputImage.get(i).get(j).getBrightness()));
            }
        }
        sin.setImage(wt);

    }

}
