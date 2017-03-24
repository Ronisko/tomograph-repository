package sample;

public class Pair {
    private double brightness;
    private int size;

    Pair() {
        this.brightness = 0.0;
        this.size = 0;
    }

    public void increaseBrightness(double b) {
        this.brightness += b;
        this.size++;
    }

    public double getBrightness() {
        return this.brightness/this.size;
    }
}
