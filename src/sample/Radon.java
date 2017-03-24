package sample;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Radon {

    private static Emitter emitter;
    private static List<Detector> detectors;

    private static List<List<Double>> brightnesseses = new ArrayList<>();


    public static void radonTransform(Image image, PixelWriter pixelWriter, ImageView sin, WritableImage wt, Image myImage) {
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
                lines.add(bresenham.bresenhamLine((int)emitter.getX(), (int)emitter.getY(), (int)detectors.get(j).getX(), (int)detectors.get(j).getY()));
            }
            brightnesseses.add(lines);
        }

        for (int k = 0; k < Input.getEmittersNumber(); k++) {
            for (int i = 0; i < Input.getDetectorsNumber(); i++) {
                pixelWriter.setColor(i, k, Color.hsb(0, 0.0, ((brightnesseses.get(k).get(i)-Bresenham.getMinBrightness())/(Bresenham.getMaxBrightness()-Bresenham.getMinBrightness()))));
            }
        }
        sin.setImage(wt);

    }

    public void inverseRadonTransform() {

    }

}
