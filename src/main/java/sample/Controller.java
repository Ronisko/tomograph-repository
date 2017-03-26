package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.dcm4che2.data.*;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.util.UIDUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @FXML
    private Button dicomButton;

    @FXML
    private Text nazwa;

    @FXML
    private Text sex;

    @FXML
    private Text birthDate;

    @FXML
    private Text comment;

    @FXML
    private DatePicker birthDateText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField sexText;

    @FXML
    private TextArea commentText;


    private File file;
    private Stage stage;
    private FileChooser fileChooser = new FileChooser();
    private static PixelReader imageReader, sinogramReader;
    private static PixelWriter sinogramWriter;
    public static List<PixelWriter> outputImageWriter;
    private static WritableImage writableSinogram, writableOutputImage;
    private static List<WritableImage> writableout;
    private static int size;



    @FXML
    private void handleDicomButton() {
        File saveas = new File("super.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableout.get(10), null), "png", saveas);
        } catch (IOException e) {
        }
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(new File("super.png"));
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            ImageIO.write(newBufferedImage, "jpg", new File("super.jpg"));
        } catch (IOException e) {

            e.printStackTrace();

        }
        File jpgSource = new File("super.jpg");
        File dcmDestination = new File("picture.dcm");
        try {
            BufferedImage jpegImage = ImageIO.read(jpgSource);
            if (jpegImage == null)
                throw new Exception("Invalid file.");
            int colorComponents = jpegImage.getColorModel().getNumColorComponents();
            int bitsPerPixel = jpegImage.getColorModel().getPixelSize();
            int bitsAllocated = (bitsPerPixel / colorComponents);
            int samplesPerPixel = colorComponents;

            DicomObject dicom = new BasicDicomObject();

            String oldstring = birthDateText.getValue().toString() + " 00:00:00.0";
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring);

            dicom.putString(Tag.StudyComments, VR.LT, commentText.getText());
            dicom.putDate(Tag.StudyDate, VR.DA, date);
            dicom.putString(Tag.PatientSex, VR.CS, sexText.getText());
            dicom.putDate(Tag.PatientBirthDate, VR.DA, new Date());
            dicom.putString(Tag.PatientName, VR.CS, nameText.getText());
            dicom.putString(Tag.SpecificCharacterSet, VR.CS, "ISO_IR 100");
            dicom.putString(Tag.PhotometricInterpretation, VR.CS, samplesPerPixel == 3 ? "MONOCHROME2" : "MONOCHROME2");
            dicom.putInt(Tag.SamplesPerPixel, VR.US, samplesPerPixel);
            dicom.putInt(Tag.Rows, VR.US, jpegImage.getHeight());
            dicom.putInt(Tag.Columns, VR.US, jpegImage.getWidth());
            dicom.putInt(Tag.BitsAllocated, VR.US, bitsAllocated);
            dicom.putInt(Tag.BitsStored, VR.US, bitsAllocated);
            dicom.putInt(Tag.HighBit, VR.US, bitsAllocated-1);
            dicom.putInt(Tag.PixelRepresentation, VR.US, 0);
            dicom.putDate(Tag.InstanceCreationDate, VR.DA, new Date());
            dicom.putDate(Tag.InstanceCreationTime, VR.TM, new Date());
            dicom.putString(Tag.StudyInstanceUID, VR.UI, UIDUtils.createUID());
            dicom.putString(Tag.SeriesInstanceUID, VR.UI, UIDUtils.createUID());
            dicom.putString(Tag.SOPInstanceUID, VR.UI, UIDUtils.createUID());
            dicom.initFileMetaInformation(UID.JPEGBaseline1);
            FileOutputStream fos = new FileOutputStream(dcmDestination);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DicomOutputStream dos = new DicomOutputStream(bos);
            dos.writeDicomFile(dicom);
            dos.writeHeader(Tag.PixelData, VR.OB, -1);
            dos.writeHeader(Tag.Item, null, 0);
            int jpgLen = (int) jpgSource.length();
            dos.writeHeader(Tag.Item, null, (jpgLen+1)&~1);
            FileInputStream fis = new FileInputStream(jpgSource);
            BufferedInputStream bis = new BufferedInputStream(fis);

            DataInputStream dis = new DataInputStream(bis);

            byte[] buffer = new byte[5000000];
            int b;
            while ((b = dis.read(buffer)) > 0) {
                dos.write(buffer, 0, b);
            }
            if ((jpgLen&1) != 0) dos.write(0);

            dos.writeHeader(Tag.SequenceDelimitationItem, null, 0);
            dos.close();
        } catch(Exception e) {
            System.out.println("ERROR: "+ e.getMessage());
        }

    }

    @FXML
    private void handleImageViewAction() {
        fileChooser.setTitle("Open image");
        file = fileChooser.showOpenDialog(stage);
        myImage = new Image(file.toURI().toString());
        myImageView1.setImage(myImage);
    }

    @FXML
    private void handleButtonAction() {
        writableout = new ArrayList<>();
        outputImageWriter = new ArrayList<>();
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
        dicomButton.setVisible(true);
        nazwa.setVisible(true);
        sex.setVisible(true);
        birthDate.setVisible(true);
        comment.setVisible(true);
        birthDateText.setVisible(true);
        sexText.setVisible(true);
        nameText.setVisible(true);
        commentText.setVisible(true);

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
