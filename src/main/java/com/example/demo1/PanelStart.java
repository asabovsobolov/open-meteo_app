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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PanelStart {

    //consts
    public static String[] dataTypes = {"Forecast", "Archive"};
    public static String[] dataTypesSRC = {"https://api.open-meteo.com/v1/forecast?", "https://archive-api.open-meteo.com/v1/archive?"};
    public static String[] locationTypes = {"Latitude, Longitude", "City"};
    public static JsonNode cities = null;

    //GUI
    @FXML private Button buttonChart;
    @FXML private DatePicker dateStart;
    @FXML private DatePicker dateEnd;
    @FXML private ChoiceBox choiceDataType;
    @FXML private ChoiceBox choiceLocationType;
    @FXML private ChoiceBox choiceCity;
    @FXML private TextField numericLatitude;
    @FXML private TextField numericLongitude;
    @FXML private Label labelLatitude;
    @FXML private Label labelLongitude;
    @FXML private Label labelCity;

    public void Init(){
        //cities
        ObjectMapper mapper = new ObjectMapper();
        if(cities == null){
            try{
                cities = mapper.readTree(Main.class.getResource("cities.json"));
                if(!cities.isArray())
                    cities = null;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        if(cities == null)
            try {
                cities = mapper.readTree("[ { \"name\":\"Hyperborea\", \"latitude\":0, \"longitude\":0 } ]");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        //dates
        LocalDate today = LocalDate.now();
        dateStart.setValue(today.minusDays(1)); // Yesterday
        dateEnd.setValue(today.plusDays(1));    // Tomorrow

        //choiceLocationType
        choiceLocationType.getItems().addAll(locationTypes);
        choiceLocationType.getSelectionModel().select(0);

        //choiceDataType
        choiceDataType.getItems().addAll(dataTypes);
        choiceDataType.getSelectionModel().select(0);

        //choiceCity
        String[] arr = new String[cities.size()];
        for(int i = 0; i < cities.size(); i++)
            arr[i] = cities.get(i).get("name").asText();
        choiceCity.getItems().addAll(arr);
        choiceCity.getSelectionModel().select(0);

        //numeric types
        numericLatitude.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        numericLongitude.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));

        //numericLongitude.setVisible(false);
        //numericLatitude.setVisible(false);
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

    @FXML protected void onChoiceLocationTypeSelect() {
        //value
        boolean b = choiceLocationType.getSelectionModel().getSelectedIndex() == 0;
        //first option
        labelLatitude.setVisible(b);
        labelLongitude.setVisible(b);
        numericLatitude.setVisible(b);
        numericLongitude.setVisible(b);
        //switch
        b = !b;
        //second option
        labelCity.setVisible(b);
        choiceCity.setVisible(b);
    }

    @FXML protected void onChoiceCitySelect() {
        //get index
        int i = choiceCity.getSelectionModel().getSelectedIndex();
        if(i < 0)
            return;

        //get city
        JsonNode city = cities.get(i);

        //update numerics
        numericLatitude.setText(city.get("latitude").asText());
        numericLongitude.setText(city.get("longitude").asText());
    }

    //Chart button clicked
    @FXML protected void onButtonChartClicked() {
        //compose API request
        int i = choiceDataType.getSelectionModel().getSelectedIndex();
        if(i < 0)
            return;
        String url = dataTypesSRC[i];
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