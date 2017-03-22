package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    private File file;

    FileChooser fileChooser = new FileChooser();

    private Stage stage;

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
        double alfa = 360/numberOfEmitters;
        int fi = Integer.parseInt(myTextField3.getText());
        double r = myImage.getWidth()/2;

        Emitter emitter = new Emitter(r*Math.cos(alfa), r*Math.sin(alfa));
        for (int i = 0; i < numberOfDetectors; i ++) {
            Detector detector = new Detector(r*Math.cos(alfa + Math.PI - fi/2 + i*(fi/numberOfDetectors)) , r*Math.sin(alfa + Math.PI - fi/2 + i*(fi/numberOfDetectors)));

        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
