package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{

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

    @FXML
    private Slider slider;

    private File file;
    private Stage stage;
    private FileChooser fileChooser = new FileChooser();
    private static PixelReader imageReader, sinogramReader;
    private static PixelWriter sinogramWriter;
    public static List<PixelWriter> outputImageWriter = new ArrayList<>();
    private static WritableImage writableSinogram, writableOutputImage;
    private static List<WritableImage> writableout = new ArrayList<>();
    private static int size;



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

        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(100);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(10);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);


        slider.setVisible(true);
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
        int height = (int)writableSinogram.getHeight();
        byte[] buffer = new byte[width * height * 4];
        sinogramReader.getPixels(0,0, width, height, PixelFormat.getByteBgraInstance(), buffer,0,width * 4);
    }



    private void makeSinogram() {
        writableSinogram = new WritableImage(Input.getEmittersNumber(), Input.getDetectorsNumber());
        sinogramWriter = writableSinogram.getPixelWriter();
        sinogram.setImage(writableSinogram);

        Radon.radonTransform();
    }

    private void makeOutputImage() {
        for (int i = 0; i <= 10; i ++) {
            writableout.add(new WritableImage((int)myImage.getWidth(), (int)myImage.getHeight()));
            outputImageWriter.add(writableout.get(i).getPixelWriter());
        }
        output.setImage((writableout.get(10)));

        Radon.inverseRadonTransform((int)myImage.getHeight());
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

    public static PixelWriter getSinogramWriter() {
        return sinogramWriter;
    }

    public static List<PixelWriter> getOutputImageWriter() {
        return outputImageWriter;
    }

    public static int getSize() {
        return size;
    }
    private void prepareInput() {
        size = (int)myImage.getHeight();
        Bresenham.setMyImage(myImage);
        Input.setNumbers(Integer.parseInt(myTextField1.getText()), Integer.parseInt(myTextField2.getText()));
        Input.setAll((2*Math.PI)/Input.getEmittersNumber(), Math.toRadians(Double.parseDouble(myTextField3.getText())), (int)myImage.getHeight()/2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                switch (t1.intValue()) {
                    case 0:
                        output.setImage(writableout.get(0));
                        break;
                    case 10:
                        output.setImage(writableout.get(1));
                        break;
                    case 20:
                        output.setImage(writableout.get(2));
                        break;
                    case 30:
                        output.setImage(writableout.get(3));
                        break;
                    case 40:
                        output.setImage(writableout.get(4));
                        break;
                    case 50:
                        output.setImage(writableout.get(5));
                        break;
                    case 60:
                        output.setImage(writableout.get(6));
                        break;
                    case 70:
                        output.setImage(writableout.get(7));
                        break;
                    case 80:
                        output.setImage(writableout.get(8));
                        break;
                    case 90:
                        output.setImage(writableout.get(9));
                        break;
                    case 100:
                        output.setImage(writableout.get(10));
                        break;
                }
            }
        });
    }
}
