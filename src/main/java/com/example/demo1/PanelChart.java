package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.application.Platform;

public class PanelChart {

    @FXML
    private Label testText;
    @FXML
    private LineChart<String, Number> lineChart;

    void Init(String url){
        new Fetcher().fetchData("https://api.github.com", new DataCallback() {
            @Override
            public void onSuccess(String data) {
                ProcessData(data);
            }

            @Override
            public void onFailure(Exception e) {
                testText.setText("Failed: " + e.getMessage());
            }
        });
    }

    void ProcessData(String response){
        testText.setText(response);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("gagaga");
        series.getData().add(new XYChart.Data<>("1", 5));
        series.getData().add(new XYChart.Data<>("2", 10));
        series.getData().add(new XYChart.Data<>("3", 20));
        series.getData().add(new XYChart.Data<>("4", 13));
        series.getData().add(new XYChart.Data<>("5", 12));
        series.getData().add(new XYChart.Data<>("6", 1));
        lineChart.getData().add(series);

        series = new XYChart.Series<>();
        series.setName("gagaga");
        series.getData().add(new XYChart.Data<>("-1", 5));
        series.getData().add(new XYChart.Data<>("2", 30));
        series.getData().add(new XYChart.Data<>("3", 30));
        series.getData().add(new XYChart.Data<>("4", 33));
        series.getData().add(new XYChart.Data<>("5", 32));
        series.getData().add(new XYChart.Data<>("6", 3));

        lineChart.getData().add(series);
    }

}
