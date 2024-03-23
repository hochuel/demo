package com.example.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static void main(String[] args){
        /*
        LocalDateTime now = LocalDateTime.now();
        String convertedDate1 = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(convertedDate1);



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(convertedDate1, formatter);


        LocalDateTime result = dateTime.plusHours(24);
        System.out.println(result);
*/


        System.out.println(DateUtils.getCurrentDateTime("yyyyMMdd"));
    }


    /**
     * yyyy-MM-dd HH:mm:ss
     * @param patten
     * @return
     */
    public static String getCurrentDateTime(String patten){
        LocalDateTime now = LocalDateTime.now();
        String convertedDate = now.format(DateTimeFormatter.ofPattern(patten));
        return convertedDate;
    }

    /**
     * 현재 날짜 시간 분 초
     * @return
     */
    public static String getCurrentDateTime(){
        return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
    }


    public static String getCurrentDate(String patten){
        LocalDate today = LocalDate.now();
        return today.format(DateTimeFormatter.ofPattern(patten));
    }

    public static String getCurrentDate(){
        return getCurrentDate("yyyy-MM-dd");
    }

    public static LocalDateTime getStringToLocalDateTime(String toDate, String patten){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patten);
        return LocalDateTime.parse(toDate, formatter);
    }

    /**
     * yyyy-MM-dd HH:mm:ss : 기본모드
     * @param toDate
     * @return
     */
    public static LocalDateTime getStringToLocalDateTime(String toDate){
        return getStringToLocalDateTime(toDate, "yyyy-MM-dd HH:mm:ss");
    }


    public static String getPlusDate(String toDate, String patten, int num){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patten);
        LocalDate localDate = LocalDate.parse(toDate, formatter).plusDays(num);

        return localDate.format(formatter);
    }


    public static String getMinusDate(String toDate, String patten, int num){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patten);
        LocalDate localDate = LocalDate.parse(toDate, formatter).minusDays(num);

        return localDate.format(formatter);
    }

}
