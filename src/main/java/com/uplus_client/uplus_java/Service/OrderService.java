package com.uplus_client.uplus_java.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uplus_client.uplus_java.Repository.OrderRepository;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class OrderService {
    @Value("${endpoint}")
    private String endpoint;

    @Value("${authpoint}")
    private String authpoint;

    @Value("${hospital_id}")
    private String hospital_id;

    Logger logger = LogManager.getLogger(OrderService.class);

    @Autowired
    OrderRepository orderRepository;

    public ResponseEntity<Map<String, Object>> OnOrderService() {
        Gson gson = new Gson();
        Response server_response = null;
        Map<String, Object> response_data = null;

        try {
            // Declare URL to request to the endpoint server.
            URL url = new URL(endpoint + "order");

            // Get data from database.
            List<LinkedHashMap<String, Object>> result = orderRepository.getOrderDataFromDB();

            // Prepare data for sending to the server.
            String reqBody = gson.toJson(result);

            // Use OkHttpClient to get request from the server.
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            // MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("hospital_id", hospital_id)
                    .addFormDataPart("data", (String) reqBody)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .build();
            // Receieve response from the server.
            server_response = client.newCall(request).execute();

            // Check the response status when success.
            if (server_response.isSuccessful()) {
                // Declare type for specific type in json form.
                Type type = new TypeToken<Map<String, Object>>() {
                }.getType();

                // Convert data from server to json form.
                response_data = gson.fromJson(server_response.body().string(), type);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ProtocolException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response_data);
    }
}
