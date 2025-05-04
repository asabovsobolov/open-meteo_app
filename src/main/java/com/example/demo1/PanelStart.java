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

    //consts
    public static String[] dataTypes = {"Forecast", "Archive"};
    public static String[] dataTypesSRC = {"https://api.open-meteo.com/v1/forecast?", "https://archive-api.open-meteo.com/v1/archive?"};

    //GUI
    @FXML private Button buttonChart;
    @FXML private DatePicker dateStart;
    @FXML private DatePicker dateEnd;
    @FXML private ChoiceBox choiceType;
    @FXML private TextField numericLatitude;
    @FXML private TextField numericLongitude;

    public void Init(){
        //dates
        LocalDate today = LocalDate.now();
        dateStart.setValue(today.minusDays(1)); // Yesterday
        dateEnd.setValue(today.plusDays(1));    // Tomorrow

        //choiceType
        choiceType.getItems().addAll(dataTypes);
        choiceType.setValue(dataTypes[0]);

        //numeric types
        numericLatitude.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        numericLongitude.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
    }

    private static String findDataTypeName(String dataType) {
        for(int i = 0; i < dataTypes.length; i++)
            if(dataTypes[i].equalsIgnoreCase(dataType))
                return dataTypesSRC[i];
        return null; // or throw an exception if not found
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
        String url = findDataTypeName(choiceType.getValue().toString());
        if(url == null){
            //error
            return;
        }

        //latidue longitude
        url += "latitude=" + numericLatitude.getText() + "&longitude=" + numericLongitude.getText();

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