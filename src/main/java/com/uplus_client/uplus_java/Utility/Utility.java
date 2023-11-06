package com.uplus_client.uplus_java.Utility;

public final class Utility {
    static{
        
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
    public static boolean isDateNull(String date){
        if(date == null)
            return true;
        else
            return false;
    }
}
