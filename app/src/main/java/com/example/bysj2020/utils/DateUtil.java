package com.example.bysj2020.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author : wxz
 * Time   : 2020/02/11
 * Desc   : 时间工具
 */
public class DateUtil {
    /**
     * string to date
     *
     * @param str
     * @param format
     * @return
     */
    public static Date stringToDate(String str, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * date to string
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * long to string
     *
     * @param currentTime
     * @param format
     * @return
     */
    public static Date longToDate(long currentTime, String format) {
        Date dateOld = new Date(currentTime); //根据long类型的毫秒数生成一个date类型的时间
        String sDateTime = dateToString(dateOld, format);  //把date类型的时间转换为string
        Date date = stringToDate(sDateTime, format);  //把string类型转换为date类型
        return date;
    }

    /**
     * date to string
     *
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * long to string
     *
     * @param currentTime
     * @param format
     * @return
     */
    public static String longToString(long currentTime, String format) {
        Date date = longToDate(currentTime, format); //long 类型转换成date类型
        String strTime = dateToString(date, format); //date类型转换成string
        return strTime;
    }

    /**
     * 倒计时 以“：”为分隔符
     *
     * @param currentTime
     * @return
     */
    public static String longToString(long currentTime) {
        String hour;
        String minute;
        String second;
        currentTime /= 1000;
        hour = currentTime / 3600 < 10 ? "0" + currentTime / 3600 : "" + currentTime / 3600;
        minute = (currentTime % 3600) / 60 < 10 ? "0" + (currentTime % 3600) / 60 : "" + (currentTime % 3600) / 60;
        second = currentTime % 3600 % 60 < 10 ? "0" + currentTime % 3600 % 60 : "" + currentTime % 3600 % 60;
        return hour + ":" + minute + ":" + second;
    }

    /**
     * 倒计时 以“时分秒”为分隔符
     *
     * @param currentTime
     * @return
     */
    public static String chatLongToString(long currentTime) {
        String hour;
        String minute;
        String second;
        currentTime /= 1000;
        hour = currentTime / 3600 < 10 ? "0" + currentTime / 3600 : "" + currentTime / 3600;
        minute = (currentTime % 3600) / 60 < 10 ? "0" + (currentTime % 3600) / 60 : "" + (currentTime % 3600) / 60;
        second = currentTime % 3600 % 60 < 10 ? "0" + currentTime % 3600 % 60 : "" + currentTime % 3600 % 60;
        if (hour.equals("00")) {
            if (minute.equals("00")) {
                return second + "秒";
            }
            return minute + "分" + second + "秒";
        }
        return hour + "时" + minute + "分" + second + "秒";
    }

    /**
     * string to long
     *
     * @param str
     * @param format
     * @return
     */
    public static long stringToLong(String str, String format) {
        Date date = stringToDate(str, format);  //string 类型转换成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date);  //date类型转换成long类型
            return currentTime;
        }
    }

    /**
     * 与当前时间间隔天数
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToNumber(long timeStamp) {
        long time = (timeStamp - System.currentTimeMillis()) / 1000;
        return time / (3600 * 24) < 0 ? "0" : time / (3600 * 24) + "";
    }
}
