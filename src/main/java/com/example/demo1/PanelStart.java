package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class PanelStart {
    @FXML
    private Label welcomeText;

    private static int i = 0;
    private final RedisCacheService cacheService = new RedisCacheService();

    @FXML
    protected void onHelloButtonClick() {
        //cacheService.getCachedData("gag");
        //welcomeText.setText(cacheService.getCachedData("gag"));
        //cacheService.cacheData("gag","hello",10);

        try {
            // Load the FXML file for the new window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PanelChart.fxml"));
            AnchorPane root = fxmlLoader.load(); // Load the layout from the FXML file

            //init
            PanelChart controller = fxmlLoader.getController();
            controller.Init(String.valueOf(i++));

            // Create a new Stage (window) for the new panel
            Stage stage = new Stage();
            stage.setTitle("Chart Panel");

            // Set the scene with the loaded layout
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the new window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}