package open_meteo_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("PanelStart.fxml"));
        AnchorPane root = fxmlLoader.load(); // Load the layout from the FXML file

        //init
        PanelStart controller = fxmlLoader.getController();
        controller.Init();

        // Create a new Stage (window) for the new panel
        stage.setTitle("Open-meteo App");

        // Set the scene with the loaded layout
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Show the new window
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}