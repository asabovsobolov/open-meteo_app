module open_meteo_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    //
    requires redis.clients.jedis;
    requires com.fasterxml.jackson.databind;
    requires com.opencsv;

    opens open_meteo_app to javafx.fxml;
    exports open_meteo_app;
}