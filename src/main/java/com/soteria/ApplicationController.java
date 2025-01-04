package com.soteria;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    @FXML
    private VBox cameraList;
    private final List<Camera> cameras = new ArrayList<>();

    void addCamera(Camera camera) {
        cameras.add(camera);
        cameraList.getChildren().add(new Text(camera.ipAddress()));
    }

    @FXML
    protected void openAddCamera() {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("addcamera.fxml"));
        Stage stage = new Stage();
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), 320, 240);
        } catch (IOException e) {
            // TODO: Add logging
        }

        stage.setTitle("Add camera");
        stage.setScene(scene);
        stage.show();
    }
}