package com.uplus_client.uplus_java.Utility;

import java.time.LocalDateTime;

public final class Utility {

    static{
        
    }

    public static String getCurrentTime(){
        return LocalDateTime.now().toString();
    }

    public static String getYearFromDate(String date){
        return date.substring(0, 4);
    }

    public static String getMonthFromDate(String date){
        return date.substring(5,7);
    }

    public static String getDayFromDate(String date){
        return date.substring(8,10);
    }
    
    public static String getTimezone(String date, String time){
        return date + "T" + time + "Z";
    }

    public static String getDateFormat(){
        return "dd/mm/yyyy";
    }

    public static boolean isNotNull(String args){
        if(args == null || "".equals(args))
            return false;
        else
            return true;
    }
}
