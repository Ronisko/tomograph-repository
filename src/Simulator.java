import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Simulator extends Application {

    private File file;
    private ImageView inputImage = new ImageView("Kolo.jpg");
    private ImageView sinogram = new ImageView("Kolo.jpg");
    private ImageView outputImage = new ImageView("Kolo.jpg");
    private TextField numberOfEmittersText = new TextField();
    private TextField numberOfDetectorsText= new TextField();
    private TextField obtuseness = new TextField();
    private Button button = new Button();
    private Image image;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tomograph Simulator");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open image");

        inputImage.setPreserveRatio(true);
        inputImage.setFitWidth(200);
        sinogram.setPreserveRatio(true);
        sinogram.setFitWidth(200);
        outputImage.setPreserveRatio(true);
        outputImage.setFitWidth(200);

        inputImage.setOnMouseClicked(e -> {
            file = fileChooser.showOpenDialog(primaryStage);
            image = new Image(file.toURI().toString());
            inputImage.setImage(image);
        });

        Group root = new Group();
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        HBox hBox3 = new HBox();

        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox1, hBox2, hBox3);
        vBox.setSpacing(20);

        hBox1.setSpacing(100);
        hBox2.setSpacing(20);

        hBox1.getChildren().addAll(inputImage, sinogram, outputImage);
        hBox2.getChildren().addAll(numberOfDetectorsText, numberOfEmittersText, obtuseness);
        hBox3.getChildren().addAll(button);

        root.getChildren().addAll(vBox);

        button.setOnAction(e -> {
            int numberOfDetectors = Integer.parseInt(numberOfDetectorsText.getText());
            int numberOfEmitters = Integer.parseInt(numberOfEmittersText.getText());
            double alfa = 360/numberOfEmitters;
            int fi = Integer.parseInt(obtuseness.getText());
            double r = image.getWidth()/2;

            Emitter emitter = new Emitter(r*Math.cos(alfa), r*Math.sin(alfa));
            for (int i = 0; i < numberOfDetectors; i ++) {
                Detector detector = new Detector(r*Math.cos(alfa + Math.PI - fi/2 + i*(fi/numberOfDetectors)) , r*Math.sin(alfa + Math.PI - fi/2 + i*(fi/numberOfDetectors)));

            }
        });



        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
