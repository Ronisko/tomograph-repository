package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Radon {

    private static Emitter emitter;
    private static List<Detector> detectors;
    private static List<List<Double>> brightnesses = new ArrayList<>();


    public static void radonTransform() {
        detectors = new ArrayList<>();

        emitter = new Emitter();
        for (int i = 0; i < Input.getDetectorsNumber(); i++) {
            detectors.add(new Detector());
        }

        for (int i = 0; i < Input.getEmittersNumber(); i++) {
            emitter.setAll(Math.floor(Input.getR()*Math.cos(i*Input.getAlfa())), Math.floor(Input.getR()*Math.sin(i*Input.getAlfa())));
            List<Double> line = new ArrayList<>();
            for (int j = 0; j < Input.getDetectorsNumber(); j++) {
                detectors.get(j).setAll(
                        Input.getR() * Math.cos(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))),
                        Input.getR() * Math.sin(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))));
                line.add(Bresenham.normal((int)emitter.getX(), (int)emitter.getY(), (int)detectors.get(j).getX(), (int)detectors.get(j).getY()));
            }
            brightnesses.add(line);
        }
        drawSinogram();

    }

    private static void drawSinogram() {
        for (int i = 0; i < Input.getEmittersNumber(); i++) {
            for (int j = 0; j < Input.getDetectorsNumber(); j++) {
                Controller.getSinogramWriter().setColor(i, j, Color.hsb(0, 0.0, ((brightnesses.get(i).get(j)-Bresenham.getMinBrightness())/(Bresenham.getMaxBrightness()-Bresenham.getMinBrightness()))));
            }
        }
    }

    private static List<List<Pair>> outputImage = new ArrayList<>();

    public static void inverseRadonTransform(int range) {
        Bresenham.setOutputImage(outputImage);
        for (int i = 0; i < range; i++) {
            List<Pair> list = new ArrayList<>();
            for (int j = 0; j < range; j++) {
                list.add(new Pair());
            }
            outputImage.add(list);
        }

        for (int i = 0; i < Input.getEmittersNumber(); i++) {
            emitter.setAll(Input.getR()*Math.cos(i*Input.getAlfa()), Input.getR()*Math.sin(i*Input.getAlfa()));
            for (int j = 0; j < Input.getDetectorsNumber(); j++) {
                detectors.get(j).setAll(
                        Input.getR() * Math.cos(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))),
                        Input.getR() * Math.sin(i*Input.getAlfa() + Math.PI - Input.getFi()/2 + j*(Input.getFi()/(Input.getDetectorsNumber()-1))));
                Bresenham.inverted((int)detectors.get(j).getX(), (int)detectors.get(j).getY(), (int)emitter.getX(), (int)emitter.getY(), j, i);
            }
        }

        double maxbright = 0.0, minbright = 1.0;
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                if (maxbright < outputImage.get(i).get(j).getBrightness()) {
                    maxbright = outputImage.get(i).get(j).getBrightness();
                }
                if (outputImage.get(i).get(j).getBrightness() > 0.001) {
                    if (minbright > outputImage.get(i).get(j).getBrightness()) {
                        minbright = outputImage.get(i).get(j).getBrightness();
                    }
                }
            }
        }
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                double bright = ((outputImage.get(i).get(j).getBrightness()-minbright)/(maxbright-minbright));
                if (bright < 0) {
                    bright = 0;
                }
                Controller.getOutputImageWriter().setColor(i, j, Color.hsb(0, 0.0, bright));
            }
        }
    }
}
