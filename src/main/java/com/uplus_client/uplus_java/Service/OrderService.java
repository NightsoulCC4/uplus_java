package com.uplus_client.uplus_java.Service;

import java.io.*;
import java.net.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uplus_client.uplus_java.Repository.OrderRepository;

@Service
public class OrderService {
    @Value("${endpoint}")
    private String endpoint;

    @Value("${authpoint}")
    private String authpoint;

    @Value("${hospital_id}")
    private String hospital_id;

    @Autowired
    OrderRepository orderRepository;

    public ResponseEntity<LinkedHashMap<String, String>> OnOrderService() {
        ResponseEntity<LinkedHashMap<String, String>> response = null;
        try {
            URL url = new URL(endpoint + "order");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            List<LinkedHashMap<String, Object>> result = orderRepository.getOrderDataFromDB();

            Map<String, Object> formData = new HashMap<>();
            formData.put("hospital_id", hospital_id);
            formData.put("data", result);

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
