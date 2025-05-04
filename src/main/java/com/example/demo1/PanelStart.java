package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.*;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class PanelStart {

    @FXML private Button buttonChart;
    @FXML private DatePicker dateStart;
    @FXML private DatePicker dateEnd;

    public void Init(){
        LocalDate today = LocalDate.now();
        dateStart.setValue(today.minusDays(1)); // Yesterday
        dateEnd.setValue(today.plusDays(1));    // Tomorrow
    }

    private void findCheckBoxes(Parent parent, List<CheckBox> checkBoxes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof CheckBox) {
                checkBoxes.add((CheckBox) node);
            } else if (node instanceof Parent) {
                findCheckBoxes((Parent) node, checkBoxes); // Recursive
            }
        }
    }

    //Chart button clicked
    @FXML protected void onButtonChartClicked() {
        //compose API request
        String url;
        url = "https://api.open-meteo.com/v1/forecast?";

        //latidue longitude
        url += "latitude=10&longitude=8";

        //interval
        url += "&hourly=";

        //params
        Boolean isAnother = false;
        List<CheckBox> checkBoxes = new ArrayList<>();
        findCheckBoxes(buttonChart.getScene().getRoot(), checkBoxes);
        for (CheckBox cb : checkBoxes) {
            //checked required
            if(!cb.isSelected())
                continue;
            //add comma
            if(isAnother)
                url += ",";
            else
                isAnother = true;
            //add param name
            url += cb.getUserData().toString();
        }

        //add dates
        url += "&start_date=";
        url += dateStart.getValue().toString();
        url += "&end_date=";
        url += dateEnd.getValue().toString();

        System.out.println(url);

        //cacheService.getCachedData("gag");
        //welcomeText.setText(cacheService.getCachedData("gag"));
        //cacheService.cacheData("gag","hello",10);
        try {

            // Load the FXML file for the new window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PanelChart.fxml"));
            AnchorPane root = fxmlLoader.load(); // Load the layout from the FXML file

            //init
            PanelChart controller = fxmlLoader.getController();
            controller.Init(url);

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