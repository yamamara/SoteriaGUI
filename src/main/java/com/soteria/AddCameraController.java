package com.soteria;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Add live changing of the current url for user
public class AddCameraController {
    @FXML
    private TextField ipAddressField;
    @FXML
    private TextField portField;
    @FXML
    private TextField pathField;

    private boolean validateIPAddress() {
        String ipRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        Pattern pattern = Pattern.compile(ipRegex);
        Matcher matcher = pattern.matcher(ipAddressField.getText());

        if (matcher.matches()) {
            return true;
        } else {
            // TODO: Handle invalid ip
            System.out.println("Invalid IP address!");
            return false;
        }
    }

    private boolean validatePort() {
        String portRegex = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        Pattern pattern = Pattern.compile(portRegex);
        Matcher matcher = pattern.matcher(portField.getText());

        if (matcher.matches()) {
            return true;
        } else {
            System.out.println("Invalid port!");
            return false;
        }
    }

    @FXML
    protected void handleOkButton() {
        if (validateIPAddress() && validatePort()) {
            Camera camera = new Camera(ipAddressField.getText(), Integer.parseInt(portField.getText()), pathField.getText());
            String rtspURL = camera.getURL();

            try {
                Process process = new ProcessBuilder("ffmpeg", "-i", rtspURL, "-t", "5", "-f", "null", "-")
                        .redirectErrorStream(true)
                        .start();

                boolean finished = process.waitFor(6, TimeUnit.SECONDS);

                if (finished) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    boolean streamIsUp = false;
                    String line;

                    while ((line = reader.readLine()) != null) {
                        if (line.contains("frame=")) {
                            streamIsUp = true;
                            break;
                        }
                    }

                    process.destroy();

                    if (streamIsUp) {
                        System.out.println("RTSP stream is up!");
                        Application.getMainController().addCamera(camera);
                        Stage stage = (Stage) ipAddressField.getScene().getWindow();
                        stage.close();
                    } else {
                        System.out.println("Failed to connect to the RTSP stream.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
