package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HelloController {
    @FXML
    private Label welcomeText;

    private final RedisCacheService cacheService = new RedisCacheService();

    @FXML
    protected void onHelloButtonClick() {
        //cacheService.getCachedData("gag");
        welcomeText.setText(cacheService.getCachedData("gag"));
        cacheService.cacheData("gag","hello",10);
    }
}