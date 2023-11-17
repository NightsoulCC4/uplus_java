package com.uplus_client.uplus_java.Service.U_Plus;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.uplus_client.uplus_java.Repository.H2.H2DischargeRepository;
import com.uplus_client.uplus_java.Repository.IMED.DischargeRepository;
import com.uplus_client.uplus_java.Utility.ProviderUtility;
import com.uplus_client.uplus_java.Utility.Utility;

@Service
public class DischargeService {
    @Value("${endpoint}")
    private String endpoint;

    @Value("${hospital_id}")
    private String hospital_id;

    Logger log = LogManager.getLogger(DischargeService.class);

    @Autowired
    DischargeRepository dischargeRepository;

    @Autowired
    H2DischargeRepository h2DischargeRepository;

    @Autowired
    ProviderUtility providerUtility;

    public DischargeService() {

    }

    public ResponseEntity<Map<String, Object>> onDischargeService(String access_token) {
        Gson gson = new Gson();
        Map<String, Object> response_data = null;

        try {
            // Declare URL to request to the endpoint server.
            String url = endpoint + "discharge";

            // Get data from database.
            List<LinkedHashMap<String, Object>> result = dischargeRepository.getDischargeDataFromDB();
            String backup_data = h2DischargeRepository.getDischargeBackupData();

            // Prepare data for sending to the server.
            String reqBody = gson.toJson(result);

            // If the h2 database from its table is empty. It will create new data for
            // comparing.
            if (!Utility.isNotNull(backup_data))
                h2DischargeRepository.putDischargeBackupData(reqBody);

            // If the result from imed changes. It will send the data to U-Plus and back up
            // in h2 database.
            if (!backup_data.equals(reqBody)) {
                response_data = providerUtility.provider(url.toString(), reqBody, "POST", access_token);
                if (response_data.get("status").equals("success"))
                    h2DischargeRepository.putDischargeBackupData(reqBody);
            } else {
                log.info("\n--------------------Discharge data is up to date.--------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response_data);
    }
}
