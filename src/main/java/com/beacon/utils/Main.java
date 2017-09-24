package com.beacon.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by LK on 2017/6/13.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        /*String str="2017-07-03T02:08:45Z";
        Instant instant=Instant.parse(str);
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        String date = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));*/
        /*System.out.println(getXorY(0.0));
        System.out.println(getXorY1(2.1));*/
        String s = transTime1("2017-07-17T18:50:05Z");
        System.out.println(s);
    }

    public static String getTime(){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  sdf.format(new Date());
    }


    public static double getXorY(Double i){
        String t= i.toString();
        t=t.substring(0,t.indexOf("."));
        int d=Integer.parseInt(t);
        return (d/3)*3+1.5;
    }

    public static double getXorY1(Double i){
        return (double) ((i/3+1)*3)-1.5;
    }

    public static String transTime(String time){
        Instant instant=Instant.parse(time);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        String date = ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return date;
    }

    public static String transTime1(String time){
        String s1=time.substring(0, time.indexOf("T"));
        String s2=time.substring(time.indexOf("T") + 1, time.indexOf("Z"));
        Date date = string2Date(s1 + " " + s2, "yyyy-MM-dd HH:mm:ss");
        int inteval=8*60*60*1000;
        Long time1=date.getTime()+inteval;
        return getStringOfTime(time1);
    }

    public static String getStringOfTime(long time){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }

    public static Date string2Date(String strDate,String patten) {
        DateFormat format = new SimpleDateFormat(patten);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
