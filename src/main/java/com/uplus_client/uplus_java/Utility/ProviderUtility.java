package com.uplus_client.uplus_java.Utility;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class ProviderUtility {

    @Value("${hospital_id}")
    private String hospital_id;

    Logger log = LogManager.getLogger(ProviderUtility.class);

    Gson gson = new Gson();

    ProviderUtility() {

    }

    public Map<String, Object> tokenProvider(String endpoint, String reqHeader, String reqBody, String method){
        Map<String, Object> response = new LinkedHashTreeMap<>();

        return response;
    }

    public Map<String, Object> provider(String endpoint, String reqBody, String method) {
        Map<String, Object> response = new LinkedHashTreeMap<>();

        try {
            URL url = new URL(endpoint);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("hospital_id", hospital_id)
                    .addFormDataPart("data", (String) reqBody)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method(method, body)
                    .build();

            Response server_response = client.newCall(request).execute();

            // Declare type for specific type in json form.
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            // Convert data from server to json form.
            response = gson.fromJson(server_response.body().string(), type);

            if (server_response.isSuccessful()) {
                response.put("status", "success");
                log.info("\n----------SUCCESSFUL----------");
            } else {
                response.put("status", "failed");
                log.info("\n----------FAILED----------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
