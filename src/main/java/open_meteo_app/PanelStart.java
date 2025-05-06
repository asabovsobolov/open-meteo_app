package open_meteo_app;

import javafx.fxml.FXML;
import javafx.scene.control.*;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.CSVReaderHeaderAware;
import java.util.Map;
import java.io.InputStreamReader;

public class PanelStart {

    //consts
    public static final String[] dataTypes = {"Forecast", "Archive"};
    public static final String[] dataTypesSRC = {"https://api.open-meteo.com/v1/forecast?", "https://archive-api.open-meteo.com/v1/archive?"};
    public static final String[] locationTypes = {"Latitude, Longitude", "City"};
    public static final List<City> cities =  new ArrayList<>();
    public static final int maxCitiesCount = 500;

    //vars
    public static LocalDate maxDate = null;

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
        if(cities.size() <= 0){
            try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(
                    new InputStreamReader(App.class.getResourceAsStream("worldcities.csv")))) {
                Map<String, String> row;
                int i = 0;
                while ((row = reader.readMap()) != null) {
                    cities.add(new City(row.get("city"), Double.parseDouble(row.get("lat")), Double.parseDouble(row.get("lng"))));
                    if(++i >= maxCitiesCount)
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(cities.size() <= 0)
            cities.add(new City("Hyperborea", 0, 0));

        //choiceLocationType
        choiceLocationType.getItems().addAll(locationTypes);
        choiceLocationType.getSelectionModel().select(0);

        //choiceDataType
        choiceDataType.getItems().addAll(dataTypes);
        choiceDataType.getSelectionModel().select(0);

        //choiceCity
        String[] arr = new String[cities.size()];
        for(int i = 0; i < cities.size(); i++)
            arr[i] = cities.get(i).name;
        choiceCity.getItems().addAll(arr);
        choiceCity.getSelectionModel().select(0);

        //numeric types
        numericLatitude.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        numericLongitude.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));

        //dates
        onChoiceDataTypeSelect();
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

    @FXML protected void CorrectDates(){
        LocalDate newValue;

        //=================================================================
        newValue = dateEnd.getValue();
        if(newValue == null)
            newValue = LocalDate.now().plusDays(1);

        //check max
        if (maxDate != null)
            if (newValue.isAfter(maxDate))
                newValue = maxDate;
        //set
        dateEnd.setValue(newValue);

        //=================================================================
        newValue = dateStart.getValue();
        if(newValue == null)
            newValue = LocalDate.now().minusDays(1);

        //check max
        if (maxDate != null)
            if (newValue.isAfter(maxDate))
                newValue = dateEnd.getValue();
        //check dateEnd
        if(newValue.isAfter(dateEnd.getValue()))
            newValue = dateEnd.getValue();
        //set
        dateStart.setValue(newValue);
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
        if(b)
            onChoiceCitySelect();
    }

    @FXML protected void onChoiceCitySelect() {
        //get index
        int i = choiceCity.getSelectionModel().getSelectedIndex();
        if(i < 0)
            return;

        //get city
        City city = cities.get(i);

        //update numerics
        numericLatitude.setText(String.valueOf(city.latitude));
        numericLongitude.setText(String.valueOf(city.longitude));
    }

    @FXML protected void onChoiceDataTypeSelect() {
        //correct dates
        if(choiceDataType.getSelectionModel().getSelectedIndex() == 0){
            //Forecast
            maxDate = LocalDate.now().plusDays(15);
        }
        else{
            //Archive
            maxDate = LocalDate.now().minusDays(1);
        }

        //re-valid
        CorrectDates();
    }

    //Chart button clicked
    @FXML protected void onButtonChartClicked() {
        //compose API request
        int i = choiceDataType.getSelectionModel().getSelectedIndex();
        if(i < 0)
            return;

        //ttl
        int ttl = 0;
        if(i == 0){
            ttl = 10 * 60;
        }else{
            ttl = 2 * 60 * 60;
        }

        //url
        String url = dataTypesSRC[i];

        //title - coordinates/city
        String title = dataTypes[i] + " of ";
        if(numericLatitude.isVisible()){
            title += numericLatitude.getText() + "° ," + numericLongitude.getText() + "°";
        }
        else {
            title += cities.get(choiceCity.getSelectionModel().getSelectedIndex()).name;
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
        if(!isAnother)
            return;

        //add dates
        url += "&start_date=" + dateStart.getValue().toString() + "&end_date=" + dateEnd.getValue().toString();

        //title - dates
        title += " from " + dateStart.getValue().toString() + " to " + dateEnd.getValue().toString();

        //Create new window
        try {
            // Load the FXML file for the new window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PanelChart.fxml"));
            AnchorPane root = fxmlLoader.load(); // Load the layout from the FXML file

            //init
            PanelChart controller = fxmlLoader.getController();
            controller.Init(title, url, ttl);

            // Create a new Stage (window) for the new panel
            Stage stage = new Stage();
            stage.setTitle(title);

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