package com.soteria;

public record Camera(String ipAddress, int port, String path) {
    String getURL() {
        return "rtsp://" + ipAddress + ":" + port + "/" + path;
    }
}
