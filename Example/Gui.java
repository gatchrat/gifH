
import gifH.GifH;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class Gui extends Application {
    BorderPane root;
    Scene scene;
    File file = null;
    ArrayList<File> files;
    public static void launch() {
        Application.launch(Gui.class);
    }
    public void start(Stage stage) throws Exception {
        root = new BorderPane();
        scene = new Scene(root, 640, 480);
        stage.setTitle("GifH");
        stage.setScene(scene);
        stage.show();
        createMainMenu(stage);


    }
    private void createGenerateSettingsMenu(Stage stage) {

        resetGUI();
        HBox settingMenu = new HBox();
        HBox startMenu = new HBox();
        HBox backMenu = new HBox();
        Button generate = new Button ("Generate Gif");
        Button back = new Button("Back");
        FileChooser fileChooser = new FileChooser();
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                createMainMenu(stage);
            }
        });
        generate.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(file != null){
                    try {
                        GifH gifH = new GifH();
                    } catch (Exception e) {
                        System.out.println("Couldn't read the file");
                        e.printStackTrace();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("No File selected");

                    alert.showAndWait();
                }


            }
        });
        Button chooseFile = new Button ("Choose Files");
        chooseFile.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                fileChooser.setTitle("Open files");

                files = (ArrayList<File>) fileChooser.showOpenMultipleDialog(stage);
                System.out.println("Reading " + file.getName());


            }
        });
        startMenu.setAlignment(Pos.TOP_CENTER);
        startMenu.getChildren().add(generate);

        backMenu.setAlignment(Pos.BOTTOM_CENTER);
        backMenu.getChildren().add(back);

        settingMenu.setAlignment(Pos.CENTER);
        settingMenu.getChildren().add(chooseFile);
        root.setCenter(settingMenu);
        root.setBottom(startMenu);
        root.setLeft(backMenu);
    }
    private void createExtractSettingsMenu(Stage stage) {

        resetGUI();
        HBox settingMenu = new HBox();
        HBox startMenu = new HBox();
        HBox backMenu = new HBox();
        Button generate = new Button ("Extract Gif");
        Button back = new Button("Back");
        FileChooser fileChooser = new FileChooser();
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                createMainMenu(stage);
            }
        });
        generate.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(file != null){
                    try {
                        byte[] fileContent = Files.readAllBytes(file.toPath());
                        GifH gifH = new GifH();
                        gifH.extractFilesFromGif(fileContent);
                    } catch (Exception e) {
                        System.out.println("Couldn't read the file");
                        e.printStackTrace();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("No File selected");

                    alert.showAndWait();
                }


            }
        });
        Button chooseFile = new Button ("Choose Files");
        chooseFile.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                fileChooser.setTitle("Open file to extract");

                file = fileChooser.showOpenDialog(stage);
                System.out.println("Reading " + file.getName());


            }
        });
        startMenu.setAlignment(Pos.TOP_CENTER);
        startMenu.getChildren().add(generate);

        backMenu.setAlignment(Pos.BOTTOM_CENTER);
        backMenu.getChildren().add(back);

        settingMenu.setAlignment(Pos.CENTER);
        settingMenu.getChildren().add(chooseFile);
        root.setCenter(settingMenu);
        root.setBottom(startMenu);
        root.setLeft(backMenu);
    }
    private void resetGUI(){
        root.setLeft(null);
        root.setRight(null);
        root.setBottom(null);
        root.setCenter(null);
        root.setTop(null);
    }
    private void createMainMenu(Stage stage){
        resetGUI();
        HBox mainMenu = new HBox();

        Button generate = new Button ("Generate Gif");
        generate.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               createGenerateSettingsMenu(stage);
            }
        });
        Button extract = new Button ("Extract images from Gif");
        extract.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                createExtractSettingsMenu(stage);

            }
        });
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.getChildren().add(generate);
        mainMenu.getChildren().add(extract);
        root.setCenter(mainMenu);

    }
    
}