package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView myImageView1;

    @FXML
    private ImageView sinogram;

    @FXML
    private TextField myTextField1;

    @FXML
    private TextField myTextField2;

    @FXML
    private TextField myTextField3;

    @FXML
    private Image myImage;

//    @FXML
//    private Canvas myCanvas;

    private File file;
    private Stage stage;
    private FileChooser fileChooser = new FileChooser();
    private GraphicsContext gc;
    private Emitter emitter;
    private ArrayList<Detector> detectors;

    @FXML
    private void handleImageViewAction() {
        fileChooser.setTitle("Open image");
        file = fileChooser.showOpenDialog(stage);
        myImage = new Image(file.toURI().toString());
        myImageView1.setImage(myImage);
    }

    @FXML
    private void handleButtonAction() {
        int numberOfDetectors = Integer.parseInt(myTextField1.getText());
        int numberOfEmitters = Integer.parseInt(myTextField2.getText());
        double alfa = (2*Math.PI)/numberOfEmitters;
        double fi = Math.toRadians(Double.parseDouble(myTextField3.getText()));
        double r = myImage.getHeight()/2;

        detectors = new ArrayList<>();

        emitter = new Emitter();
        for (int i = 0; i < numberOfDetectors; i++) {
            detectors.add(new Detector());
        }

        WritableImage writableImage = new WritableImage(numberOfEmitters, numberOfDetectors);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        sinogram.setImage(writableImage);

        for (int i = 0; i < numberOfEmitters; i++) {
            emitter.setAll(Math.floor(r*Math.cos(i*alfa)), Math.floor(r*Math.sin(i*alfa)));
            Radon radon = new Radon();

            for (int j = 0; j < numberOfDetectors; j++) {
                detectors.get(j).setAll(r * Math.cos(i*alfa + Math.PI - fi/2 + j*(fi/(numberOfDetectors-1))), r * Math.sin(i*alfa + Math.PI - fi/2 + j*(fi/(numberOfDetectors-1))));
                radon.bresenhamLine((int)emitter.getX(), (int)emitter.getY(), (int)detectors.get(j).getX(), (int)detectors.get(j).getY());

            }
            radon.radonTransform(myImage, i, pixelWriter, sinogram, writableImage);

        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        gc = myCanvas.getGraphicsContext2D();
//        gc.setFill(Color.WHITE);
//        gc.fillRect(0, 0, 200, 200);
    }
}
