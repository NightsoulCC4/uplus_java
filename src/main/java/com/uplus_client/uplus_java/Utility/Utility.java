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

    public static String getYearFromDate(String year) {
        return year.substring(0, 4);
    }

    public static String getMonthFromDate(String month) {
        return monthToText(month.substring(5, 7));
    }

     public static String monthToText(String month) {
        switch(month){
            case("01"): 
                month = "มกราคม";
                break;
            case("02"):
                month = "กุมภาพันธ์";
                break;
            case("03"):
                month = "มีนาคม";
                break;
            case("04"):
                month = "เมษายน";
                break;
            case("05"): 
                month = "พฤษภาคม";
                break;
            case("06"):
                month = "มิถุนายม";
                break;
            case("07"):
                month = "กรกฏาคม";
                break;
            case("08"):
                month = "สิงหาคม";
                break;
            case("09"): 
                month = "กันยายน";
                break;
            case("10"):
                month = "ตุลาคม";
                break;
            case("11"):
                month = "พฤศจิกายน";
                break;
            case("12"):
                month = "ธันวาคม";
                break;
            default:
                break;
        }
        return month;
    }

    public static String getDayFromDate(String date) {
        return date.substring(8, 10);
    }

    public static String getTimezone(String date, String time) {
        return date + "T" + time + ".000" + "Z";
    }

    public static String getBirthDateISO(String date) {
        return date + "T00:00:00.000Z";
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
