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
    private ImageView output;

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
    private static PixelReader imageReader, sinogramReader;
    private static PixelWriter sinogramWriter, outputImageWriter;
    private static WritableImage writableSinogram, writableOutputImage;



    @FXML
    private void handleImageViewAction() {
        fileChooser.setTitle("Open image");
        file = fileChooser.showOpenDialog(stage);
        myImage = new Image(file.toURI().toString());
        myImageView1.setImage(myImage);
    }

    @FXML
    private void handleButtonAction() {
        prepareInput();
        sinogram.setImage(null);

        prepareImageReader();
        makeSinogram();

        prepareSinogramReader();
        makeOutputImage();
    }

    private void prepareImageReader() {
        imageReader = myImage.getPixelReader();
        int width = (int)myImage.getWidth();
        int height = (int)myImage.getHeight();
        byte[] buffer = new byte[width * height * 4];

        imageReader.getPixels(0,0, width, height, PixelFormat.getByteBgraInstance(), buffer,0,width * 4);
    }

    private void prepareSinogramReader() {
        sinogramReader = writableSinogram.getPixelReader();
        int width = (int)writableSinogram.getWidth();
        System.out.println();
        int height = (int)writableSinogram.getHeight();
        byte[] buffer = new byte[width * height * 4];
        sinogramReader.getPixels(0,0, width, height, PixelFormat.getByteBgraInstance(), buffer,0,width * 4);
    }



    private void makeSinogram() {
        writableSinogram = new WritableImage(Input.getDetectorsNumber(), Input.getEmittersNumber());
        sinogramWriter = writableSinogram.getPixelWriter();

        Radon.radonTransform(sinogramWriter, sinogram, writableSinogram);
    }

    private void makeOutputImage() {
        writableOutputImage = new WritableImage((int)myImage.getWidth(), (int)myImage.getHeight());
        outputImageWriter = writableOutputImage.getPixelWriter();

        Radon.inverseRadonTransform(outputImageWriter, output, writableOutputImage, (int)myImage.getHeight());

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static PixelReader getImageReader() {
        return imageReader;
    }

    public static PixelReader getSinogramReader() {
        return sinogramReader;
    }


    public static WritableImage getWritableSinogram() {
        return writableSinogram;
    }

    private void prepareInput() {
        Bresenham.setMyImage(myImage);
        Input.setNumbers(Integer.parseInt(myTextField1.getText()), Integer.parseInt(myTextField2.getText()));
        Input.setAll((2*Math.PI)/Input.getEmittersNumber(), Math.toRadians(Double.parseDouble(myTextField3.getText())), myImage.getHeight()/2);
    }
}
