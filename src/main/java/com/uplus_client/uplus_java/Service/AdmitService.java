package com.uplus_client.uplus_java.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.util.*;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.uplus_client.uplus_java.Repository.H2.H2Repository;
import com.uplus_client.uplus_java.Repository.IMED.AdmitRepository;
import com.uplus_client.uplus_java.Utility.ProviderUtility;

import okhttp3.*;

@Service
public class AdmitService {

    @Value("${endpoint}")
    private String endpoint;

    @Value("${authpoint}")
    private String authpoint;

    @Value("${hospital_id}")
    private String hospital_id;

    @Autowired
    AdmitRepository admitRepository;

    @Autowired
    H2Repository h2Repository;

    @Autowired
    ProviderUtility providerUility;

    public AdmitService() {

    }

    Logger logger = LogManager.getLogger(AdmitService.class);

    public ResponseEntity<Map<String, Object>> onAdmitService() {
        Gson gson = new Gson();
        Response server_response = null;
        Map<String, Object> response_data = null;
        
        try {
            // Declare URL to request to the endpoint server.
            URL url = new URL(endpoint + "admit");

            // Get data from database.
            List<LinkedHashMap<String, Object>> result = admitRepository.getAdmitDataFromDB();
            String backup_data = h2Repository.getBackupData();

            // Prepare data for sending to the server.
            String reqBody = gson.toJson(result);

            logger.info("\nurl: " + url);

            providerUility.provider(url.toString(), reqBody, "POST");

            // Use OkHttpClient to get request from the server.
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("hospital_id", hospital_id)
                    .addFormDataPart("data", (String) reqBody)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .build();

            // Get back up data for comparing with i-med data.
            

            // Are back_up data and req_data match?
            if (!backup_data.equals(reqBody)) 
                // Receieve response from the server.
                server_response = client.newCall(request).execute();
            
            
            // Check the response status when success.
            if (server_response != null && server_response.isSuccessful()) {
                // Declare type for specific type in json form.
                Type type = new TypeToken<Map<String, Object>>() {
                }.getType();

                // Convert data from server to json form.
                response_data = gson.fromJson(server_response.body().string(), type);

                // Save into h2 database if the request successful.
                h2Repository.putBackupData(reqBody);

                logger.info("\n----------SUCCESSFUL----------");
            } else
                logger.info("\n----------FAILED----------");

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
