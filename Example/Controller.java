import gifH.GIFSettings;
import gifH.GifH;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Controller {
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField versionField;
    @FXML
    private TextField resolutionField;
    @FXML
    private TextField applicationField;
    @FXML
    private TextField commentField;
    @FXML
    private TextField disposalField;
    @FXML
    private TextField timeField;

    @FXML
    protected void startCombination(ActionEvent event) throws IOException {
        GIFSettings settings = new GIFSettings();
        ArrayList<File> images = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open files to combine");
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        List<File> cf = fileChooser.showOpenMultipleDialog(stage);
        for (File f : cf) {
            images.add( f);
        }
        settings.applicationData = applicationField.getText();
        settings.comment = commentField.getText();
        settings.disposalMethod = Integer.parseInt(disposalField.getText());
        settings.delayTime = Integer.parseInt(timeField.getText());
        GifH gifH = new GifH();
        gifH.combineIntoGif(images, settings, "NAME");
    }

    @FXML
    protected void startExtraction(ActionEvent event) throws IOException {
        GIFSettings settings = new GIFSettings();
        File image;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file to extract");
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        image = fileChooser.showOpenDialog(stage);
        byte[] fileContent = Files.readAllBytes(image.toPath());
        GifH gifH = new GifH();
        settings = gifH.extractFilesFromGif(fileContent, settings);
        widthField.setText(String.valueOf(settings.width));
        heightField.setText(String.valueOf(settings.height));
        versionField.setText(settings.version);
        applicationField.setText(settings.applicationData);
        commentField.setText(settings.comment);
        disposalField.setText(String.valueOf(settings.disposalMethod));
        timeField.setText(String.valueOf(settings.delayTime));

    }

    @FXML
    protected void openCombination(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("combine.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        scene.setRoot(root);
    }

    @FXML
    protected void openExtraction(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("extract.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        scene.setRoot(root);
    }

    @FXML
    protected void openMain(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        scene.setRoot(root);
    }


}