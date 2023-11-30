package com.uplus_client.uplus_java.Service.U_Plus;

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
import com.uplus_client.uplus_java.Repository.IMED.OrderRepository;
import com.uplus_client.uplus_java.Utility.Utility;

//Request order data in service layer.
@Service
public class OrderService {
    @Value("${endpoint}")
    private String endpoint;

    @Value("${authpoint}")
    private String authpoint;

    @Value("${hospital_id}")
    private String hospital_id;

    Logger log = LogManager.getLogger(OrderService.class);

    @Autowired
    OrderRepository orderRepository;

    public OrderService() {

    }

    public ResponseEntity<Map<String, Object>> OnOrderService() {
        Gson gson = new Gson();
        Map<String, Object> response_data = null;

        try {
            // Declare URL to request to the endpoint server.
            URL url = new URL(endpoint + "order");

            // Get data from database.
            List<LinkedHashMap<String, Object>> result = orderRepository.getOrderDataFromDB();

            // Prepare data for sending to the server.
            String reqBody = gson.toJson(result);

            log.info("\n reqBody: " + reqBody);

            if(response_data == null)
                response_data = new LinkedHashMap<>();

            response_data.put("hospital_id", hospital_id);
            response_data.put("data", Utility.StringToMap(reqBody));

            log.info("\nurl: " + url);


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response_data);
    }
}
