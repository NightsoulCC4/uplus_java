package com.uplus_client.uplus_java.Service.U_Plus;

import java.util.*;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.*;
import com.uplus_client.uplus_java.Repository.H2.H2AdmitRepository;
import com.uplus_client.uplus_java.Repository.IMED.AdmitRepository;
import com.uplus_client.uplus_java.Utility.ProviderUtility;
import com.uplus_client.uplus_java.Utility.Utility;

// Request admit data in service layer.
@Service
public class AdmitService {

    @Value("${endpoint}")
    private String endpoint;

    @Value("${hospital_id}")
    private String hospital_id;

    @Autowired
    AdmitRepository admitRepository;

    @Autowired
    H2AdmitRepository h2AdmitRepository;

    @Autowired
    ProviderUtility providerUtility;

    public AdmitService() {

    }

    Logger log = LogManager.getLogger(AdmitService.class);

    public ResponseEntity<Map<String, Object>> onAdmitService(String access_token) {
        Gson gson = new Gson();
        Map<String, Object> response_data = null;

        try {
            // Declare URL to request to the endpoint server.
            String url = endpoint + "admit";

            // Get data from database.
            List<LinkedHashMap<String, Object>> result = admitRepository.getAdmitDataFromDB();
            String backup_data = h2AdmitRepository.getAdmitBackupData();

            // Prepare data for sending to the server.
            String reqBody = gson.toJson(result);

            // If the h2 database from its table is empty. It will create new data for comparing.
            if(!Utility.isNotNull(backup_data))
                h2AdmitRepository.putAdmitBackupData(reqBody);

            // If the result from imed changes. It will send the data to U-Plus and back up in h2 database.
            if (!backup_data.equals(reqBody)){
                response_data = providerUtility.provider(url.toString(), reqBody, "POST", access_token);
                if(response_data.get("status").equals("success"))
                    h2AdmitRepository.putAdmitBackupData(reqBody);
            } else {
                log.info("\n--------------------Admit data is up to date.--------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response_data);
    }
}
