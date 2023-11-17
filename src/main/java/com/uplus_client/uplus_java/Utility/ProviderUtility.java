package com.uplus_client.uplus_java.Utility;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;

// import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class ProviderUtility {

    @Value("${hospital_id}")
    private String hospital_id;

    @Value("${security.oauth2.client.authorized-grant-types.password}")
    private String grant_type;

    @Value("${spring.security.user.name.client.u-plus}")
    private String username;

    @Value("${spring.security.user.password.client.u-plus}")
    private String password;

    @Value("${spring.security.user.authorization_code.client.u-plus}")
    private String authorization;

    @Value("${spring.security.user.cookie.client.u-plus}")
    private String cookie;

    Logger log = LogManager.getLogger(ProviderUtility.class);

    Gson gson = new Gson();

    ProviderUtility() {

    }

    public Map<String, Object> tokenProvider(String endpoint, String method) {
        Map<String, Object> response = new LinkedHashTreeMap<>();

        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            // MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("grant_type", grant_type)
                    .addFormDataPart("username", username)
                    .addFormDataPart("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(endpoint)
                    .method(method, body)
                    .addHeader("Authorization", authorization)
                    .addHeader("Cookie", cookie)
                    .build();
            Response server_response = client.newCall(request).execute();

            String reqBody = server_response.body().string();

            log.info("\nreqBody: " + reqBody);

            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            response = gson.fromJson(reqBody, type);

            if (server_response.isSuccessful()) {
                response.put("status", "success");
                log.info("\n----------SUCCESSFUL----------");
            } else {
                response.put("status", "failed");
                log.info("\n----------FAILED----------");
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public Map<String, Object> provider(String endpoint, String reqBody, String method, String access_token) {
        Map<String, Object> response = new LinkedHashTreeMap<>();

        log.info("\nSent data from " + endpoint);
        log.info("\nreqBody: " + reqBody);

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
                    .addHeader("Authorization", "Bearer " + access_token)
                    .addHeader("Cookie", "JSESSIONID=41C042E3DEAB2AC471A5143113ED9AAC")
                    .build();

            // Execute request.
            Response server_response = client.newCall(request).execute();

            // Declare type for specific type in json form.
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            // Convert data from server to json form.
            response = gson.fromJson(server_response.body().string(), type);

            log.info("\nresponse: " + response);

            if (server_response.isSuccessful()) {
                response.put("status", "success");
                log.info("\n----------SUCCESSFUL----------");
            } else {
                response.put("status", "failed");
                log.info("\n----------FAILED----------");
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
