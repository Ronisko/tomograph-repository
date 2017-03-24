package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller  {

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
    private static PixelReader pixelReader;


    @FXML
    private void handleImageViewAction() {
        fileChooser.setTitle("Open image");
        file = fileChooser.showOpenDialog(stage);
        myImage = new Image(file.toURI().toString());
        myImageView1.setImage(myImage);
    }

    @FXML
    private void handleButtonAction() {
        Bresenham.setMyImage(myImage);
        pixelReader = myImage.getPixelReader();
        int width = (int)myImage.getWidth();
        int height = (int)myImage.getHeight();
        byte[] buffer = new byte[width * height * 4];
        pixelReader.getPixels(0,0, width, height, PixelFormat.getByteBgraInstance(), buffer,0,width * 4);
        sinogram.setImage(null);
        Input.setNumbers(Integer.parseInt(myTextField1.getText()), Integer.parseInt(myTextField2.getText()));
        Input.setAll((2*Math.PI)/Input.getEmittersNumber(), Math.toRadians(Double.parseDouble(myTextField3.getText())), myImage.getHeight()/2);

        WritableImage writableImage = new WritableImage(Input.getDetectorsNumber(), Input.getEmittersNumber());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        Radon.radonTransform(myImage, pixelWriter, sinogram, writableImage, myImage);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static PixelReader getPixelReader() {
        return pixelReader;
    }
}
