package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Radon {

    private static Emitter emitter;
    private static List<Detector> detectors;
    private static List<List<Double>> brightnesses;


    public static void radonTransform() {
        brightnesses = new ArrayList<>();
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

    private static List<List<Pair>> outputImage;

    public static void inverseRadonTransform(int range) {
        outputImage = new ArrayList<>();
        int counter = 0;
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
            if (i%(Input.getEmittersNumber()/10) == 0) {
                if (i == 0) {
                    show(range, counter);
                } else {
                    show(range, counter);
                }
                counter++;
            }
        }

        show(range, counter);

    }

    private static void show(int range, int step) {
        double maxbright = 0.0, minbright = 1.0;
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                if (maxbright < outputImage.get(i).get(j).getBrightness()) {
                    maxbright = outputImage.get(i).get(j).getBrightness();
                }
                if (outputImage.get(i).get(j).getBrightness() > 0.1) {
                    if (minbright > outputImage.get(i).get(j).getBrightness()) {
                        minbright = outputImage.get(i).get(j).getBrightness();
                    }
                }
            }

        }
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                double bright = ((outputImage.get(i).get(j).getBrightness()-minbright)/(maxbright-minbright));
                if (bright <= 0.01) {
                    bright = 0;
                }
                Controller.outputImageWriter.get(step).setColor(i, j, Color.hsb(0, 0.0, bright));
            }
        }
    }
}
