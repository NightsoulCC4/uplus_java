package com.uplus_client.uplus_java.Utility;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class Utility {

    static {

    }

    public static String getCurrentTime() {
        return LocalDateTime.now().toString();
    }

    public static String getYearFromDate(String date) {
        return date.substring(0, 4);
    }

    public static String getMonthFromDate(String date) {
        return date.substring(5, 7);
    }

    public static String getDayFromDate(String date) {
        return date.substring(8, 10);
    }

    public static String getTimezone(String date, String time) {
        return date + "T" + time + "Z";
    }

    public static String getDateFormat() {
        return "dd/mm/yyyy";
    }

    public static boolean isNotNull(String args) {
        if (args == null || "".equals(args))
            return false;
        else
            return true;
    }

    public static List<Map<String, Object>> StringToMap(String data) {
        // Create a Gson instance.
        Gson gson = new Gson();

        // Define the type for the Map.
        Type type = new TypeToken<List<Map<String, Object>>>() {
        }.getType();

        // Convert the JSON string to a Map.
        List<Map<String, Object>> resultMap = gson.fromJson(data, type);

        return resultMap;
    }
}
