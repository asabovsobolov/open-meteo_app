package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PanelChart {

    @FXML
    private Label testText;

    void Init(String url){
        testText.setText(url);
    }

}
