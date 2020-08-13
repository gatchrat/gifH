import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
public class Gui extends Application {
    public static void launch() {
        Application.launch(Gui.class);
    }
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        HBox mainMenu = new HBox();
        Scene scene = new Scene(root, 640, 480);
        Button generate = new Button ("Generate Gif");
        FileChooser fileChooser = new FileChooser();

        generate.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("ye");
            }
        });
        Button extract = new Button ("Extract images from Gif");
        extract.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                fileChooser.setTitle("Open file to extract");
                File file = fileChooser.showOpenDialog(stage);
                System.out.println("Reading " + file.getName());
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    GifExtractor ge = new GifExtractor(fileContent);
                } catch (Exception e) {
                   System.out.println("Couldnt read the file");
                   e.printStackTrace();
                }
               
            }
        });
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.getChildren().add(generate);
        mainMenu.getChildren().add(extract);
        root.setCenter(mainMenu);
        stage.setTitle("GifH");
        stage.setScene(scene);
        stage.show();

    }
    
}