import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Simulator extends Application {

    private ImageView inputImage = new ImageView("Kolo.jpg");
    private ImageView sinogram = new ImageView("Kolo.jpg");
    private ImageView outputImage = new ImageView("Kolo.jpg");

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
            fileChooser.showOpenDialog(primaryStage);
        });

        Group root = new Group();
        HBox box = new HBox();
        box.setSpacing(100);
        box.getChildren().addAll(inputImage, sinogram, outputImage);
        root.getChildren().add(box);

        Scene scene = new Scene(root, 800, 250);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
