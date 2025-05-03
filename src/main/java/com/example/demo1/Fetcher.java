package com.example.demo1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class Fetcher {
    public void fetchData(String urlString, DataCallback callback) {
        // No instance variables, so Fetcher will be GC'd after method ends if nothing else holds it
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    callback.onFailure(new RuntimeException("HTTP error code: " + responseCode));
                    return;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                in.close();

                // Run success callback on FX thread
                javafx.application.Platform.runLater(() ->
                        callback.onSuccess(content.toString())
                );
            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                        callback.onFailure(e)
                );
            }
        }).start(); // After this thread finishes and nothing references this Fetcher, it's eligible for GC
    }
}
