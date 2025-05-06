package open_meteo_app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Iterator;
import java.util.Map;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class PanelChart {

    //vars
    private JsonNode data = null;
    private String query;

    //GUI
    @FXML private Label labelTitle;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private Button buttonExport;

    //Init
    void Init(String title, String url, int ttl){
        //set title
        labelTitle.setText(title);

        //set query
        query = url;

        //Check Cache
        String cached = RedisCacheService.getCachedData(url);
        if(cached != null){
            ProcessData(cached);
            return;
        }

        //Fetch and Cache
        new Fetcher().fetchData(url, new DataCallback() {
            @Override public void onSuccess(String data) {
                //try
                if(ProcessData(data))
                    //cache
                    RedisCacheService.cacheData(url, data, ttl);
                else
                    FailedData();
            }

            @Override public void onFailure(Exception e) {
                FailedData();
            }
        });
    }

    void TrimData() {
        ObjectNode d = (ObjectNode) data;

        String[] requiredParamNames = {"hourly", "hourly_units", "time"};
        List<String> toRemove = new ArrayList<>();

        //Phase 1: Collect fields to remove
        Iterator<Map.Entry<String, JsonNode>> fields = d.fields();
        while(fields.hasNext()){
            String paramName = fields.next().getKey();
            if(!Arrays.asList(requiredParamNames).contains(paramName))
                toRemove.add(paramName);
        }

        //Phase 2: Remove fields
        for(String key : toRemove){
            d.remove(key);
        }

        //Add custom field
        d.put("title", labelTitle.getText());
        d.put("query", query);
    }

    //returns true if data is vaild
    boolean ProcessData(String response){
        try{
            //json
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            data = json;

            //units
            JsonNode units = json.get("hourly_units");

            //time
            json = json.get("hourly");
            JsonNode dx = json.get("time");
            JsonNode dy;

            //building series and adding them to chart
            XYChart.Series<String, Number> series;
            Iterator<Map.Entry<String, JsonNode>> fields = json.fields();
            int i;
            int f = dx.size();
            String paramName;
            while(fields.hasNext()){
                //get key different from time
                paramName = fields.next().getKey();
                if(paramName == "time")
                    continue;

                //read array
                dy = json.get(paramName);
                series = new XYChart.Series<>();
                for(i = 0; i < f; i++)
                    series.getData().add(new XYChart.Data<>(dx.get(i).asText(), dy.get(i).asDouble()));

                //apply meta
                series.setName(paramName + " [" + units.get(paramName).asText() + "]");

                //apply to chart
                lineChart.getData().add(series);
            }
            //trim data (so the export contains only necessary info)
            TrimData();
            //value
            return true;
        }
        catch(Exception e){}

        //value
        return false;
    }

    public void Export(){
        //block null data
        if(data == null)
            return;

        //save
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Eksportuj do...");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Plik tekstowo JSONowy", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);
        File file = fileChooser.showSaveDialog(buttonExport.getScene().getWindow());
        if(file != null){
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //Failure
    void FailedData(){
        labelTitle.setText("Failed.");
        lineChart.getData().clear();
        buttonExport.setDisable(true);
    }
}
