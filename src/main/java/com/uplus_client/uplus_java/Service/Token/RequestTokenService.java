package com.uplus_client.uplus_java.Service.Token;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uplus_client.uplus_java.Utility.ProviderUtility;

// Request token in service layer.
@Service
public class RequestTokenService {

    @Autowired
    ProviderUtility providerUtility;

    @Value("${authpoint}")
    private String authpoint;

    Logger logger = LogManager.getLogger(RequestTokenService.class);
    
    public RequestTokenService(){

    }

    public Map<String, Object> requestToken(){
        Map<String, Object> response_data = null;

        try {
            // Declare URL to request to the endpoint server.
            URL url = new URL(authpoint);

            logger.info("\nurl: " + url);

            response_data = providerUtility.tokenProvider(url.toString(), "POST");

            logger.info("\nToken: " + response_data);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } 

        return response_data;
    } 

}
