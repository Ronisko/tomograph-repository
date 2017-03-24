package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
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


    private File file;
    private Stage stage;
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private void handleImageViewAction() {
        fileChooser.setTitle("Open image");
        file = fileChooser.showOpenDialog(stage);
        myImage = new Image(file.toURI().toString());
        myImageView1.setImage(myImage);
    }

    @FXML
    private void handleButtonAction() {
        sinogram.setImage(null);
        Input.setNumbers(Integer.parseInt(myTextField1.getText()), Integer.parseInt(myTextField2.getText()));
        Input.setAll((2*Math.PI)/Input.getEmittersNumber(), Math.toRadians(Double.parseDouble(myTextField3.getText())), myImage.getHeight()/2);

        WritableImage writableImage = new WritableImage(Input.getEmittersNumber(), Input.getDetectorsNumber());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

//        for (int i = 0; i < numberOfEmitters; i++) {
//
//
//            for (int j = 0; j < numberOfDetectors; j++) {
////                detectors.get(j).setAll();
//                //dajemy wszystkie wartości do detektorów, a dopiero potem sie nimy bawimy
//            }
//
//        }
        Radon.radonTransform(myImage, pixelWriter, sinogram, writableImage);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
