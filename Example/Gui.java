
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;

import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.File;

import java.util.ArrayList;

public class Gui extends Application {
    VBox root;
    Scene scene;
    File file = null;
    ArrayList<File> files;
    public static void launch() {
        Application.launch(Gui.class);
    }
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        scene = new Scene(root, 640, 480);
        stage.setTitle("GifH");
        stage.setScene(scene);
        stage.show();
    }
}