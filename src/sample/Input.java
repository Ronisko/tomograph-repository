package sample;

public class Input {
    private static double alfa;
    private static double fi;
    private static int r;
    private static int detectorsNumber;
    private static int emittersNumber;

    public static void setNumbers(int d, int e) {
        Input.detectorsNumber = d;
        Input.emittersNumber = e;
    }
    public static void setAll(double a, double f, int r) {
        Input.alfa = a;
        Input.fi = f;
        Input.r = r;
    }

    public static double getAlfa() {
        return alfa;
    }

    public static double getFi() {
        return fi;
    }

    public static double getR() {
        return r;
    }

    public static int getDetectorsNumber() {
        return detectorsNumber;
    }

    public static int getEmittersNumber() {
        return emittersNumber;
    }
}
