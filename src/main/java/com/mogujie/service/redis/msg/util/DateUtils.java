package com.mogujie.service.redis.msg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zhizhu on 15/8/6.
 */
public class DateUtils {
    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static final String COMPACT_DATE_FORMAT = "yyyyMMdd";

    public static final String NORMAL_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 获取当前Date类型时间
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(calendar.getTime());
        return dateStr;
    }

    /**
     * 日期转unixTime
     * @param date     2015-01-15 11:48:13
     * @param format   DATETIME_FORMAT
     * @return
     */
    public static Long date2UnixTime(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Long unixTime = 0l;
        try {
            unixTime = sdf.parse(date).getTime() / 1000;
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return unixTime;
    }

    /**
     * 日期转unixTime
     * @param date     2015-01-15 11:48:13   默认 yyyy-MM-dd HH:mm:ss 格式
     * @return
     */
    public static Long date2UnixTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        Long unixTime = 0l;
        try {
            unixTime = sdf.parse(date).getTime() / 1000;
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return unixTime;
    }

    /**
     * Unix时间戳转换为指定类型
     * @param time
     * @param format
     * @return
     */
    public static String unixTime2Date(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(time*1000);
        return dateStr;
    }

    /**
     * Unix时间戳转换为指定类型
     * @param time
     * @return
     */
    public static String unixTime2Date(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        String dateStr = sdf.format(time*1000);
        return dateStr;
    }

    /**
     * 获取Unix时间戳
     * @return
     */
    public static Long getCurrentUnixTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取当月第一天 unix时间戳
     * @return
     */
    public static Long getCurrentMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取当月最后一天 unix时间戳
     * @return
     */
    public static Long getCurrentMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取当天开始时间戳
     * @return
     */
    public static Long getCurrentDayStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        return todayStart.getTimeInMillis() / 1000;
    }

    /**
     * 获取当天结束时间戳
     * @return
     */
    public static Long getCurrentDayEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        return todayEnd.getTimeInMillis() / 1000;
    }

    /**
     * 获取7天前时间戳
     * @return
     */
    public static Long getCurrentTimeBefore7Day() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, - 7);
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取30天前时间戳
     * @return
     */
    public static Long getCurrentTimeBefore30Day() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, - 30);
        return calendar.getTimeInMillis() / 1000;
    }


    public static Long getCurrentTimeBeforeDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取当前时间之前1小时
     * @return
     */
    public static Long getBeforeHourTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        return calendar.getTimeInMillis() / 1000;
    }

    public static void main(String[] args) {
        System.out.println(date2UnixTime("2015-01-15 11:48:13", DATETIME_FORMAT));
        System.out.println(getCurrentDayStartTime());
        System.out.println(getCurrentDayEndTime());
        System.out.println(getCurrentTimeBefore7Day());
        System.out.println(getCurrentTimeBefore30Day());
        System.out.println(getCurrentUnixTime());
    }
}
