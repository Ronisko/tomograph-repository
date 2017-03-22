package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TextField myTextField1;

    @FXML
    private TextField myTextField2;

    @FXML
    private TextField myTextField3;

    @FXML
    private Image myImage;

    @FXML
    private Canvas myCanvas;

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
        System.out.println(numberOfDetectors);
        int numberOfEmitters = Integer.parseInt(myTextField2.getText());
        System.out.println(numberOfEmitters);
        double alfa = (2*Math.PI)/numberOfEmitters;
        double fi = Math.toRadians(Double.parseDouble(myTextField3.getText()));
        double r = 100;

        emitter = new Emitter(r*Math.cos(alfa), r*Math.sin(alfa));
//        gc.setFill(Color.BLACK);
//        System.out.println(emitter.getX() + " " +  emitter.getY());
//        gc.fillRect(emitter.getX()+100 , emitter.getY()+100,3, 3);

        detectors = new ArrayList<>();
        for (int i = 0; i < numberOfDetectors; i ++) {
            detectors.add(new Detector(r * Math.cos(alfa + Math.PI - fi/2 + i*(fi/(numberOfDetectors-1))), r * Math.sin(alfa + Math.PI - fi/2 + i*(fi/(numberOfDetectors-1)))));
//            gc.setFill(Color.RED);
//            System.out.println(detector.getX() + " " +  detector.getY());
//            gc.fillRect(detector.getX()+100 , detector.getY()+100,3, 3);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = myCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 200, 200);
    }
}
