package com.uplus_client.uplus_java.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

public class Other {

    @Value("${endpoint}")
    private String endpoint;

    public ResponseEntity<LinkedHashMap<String, String>> OnMonitorInterfaceService() {
        ResponseEntity<LinkedHashMap<String, String>> response = null;
        try {
            URL url = new URL(endpoint + "monitorInterface");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Map<String, String> formData = new HashMap<>();
            formData.put("hospital_id", (String) "String: hospital_id");
            formData.put("data", "String");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public ResponseEntity<LinkedHashMap<String, String>> OnSummaryOrderService() {
        ResponseEntity<LinkedHashMap<String, String>> response = null;
        try {
            URL url = new URL(endpoint + "summary");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Map<String, String> formData = new HashMap<>();
            formData.put("hospital_id", (String) "String: hospital_id");
            formData.put("data", "String");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
