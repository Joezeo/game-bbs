package com.joezeo.community.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private TimeUtils() {
    }

    /**
     * 获取当天零点的时间戳
     */
    public static long getTimestampAtZero() throws ParseException {
        // 获取当天零点的时间戳
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String preDateStr = dateFormat.format(new Date(System.currentTimeMillis()));
        Date preDate = dateFormat.parse(preDateStr);
        long preTimeAtZero = preDate.getTime(); // 当天00:00时间戳
        return preTimeAtZero;
    }

    /**
     * 获取当前时间与下一个凌晨2点之间的时间差
     * 单位秒
     */
    public static int getDifftimeFromNextZero() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String preDateStr = dateFormat.format(new Date(System.currentTimeMillis())); // 当天的年月日，如：20200210
        String nextTwoStr = (Integer.parseInt(preDateStr) + 1) + "02";
        dateFormat = new SimpleDateFormat("yyyyMMddhh");
        Date nextTwoDate = dateFormat.parse(nextTwoStr);

        long nextTwoTimestamp = nextTwoDate.getTime();
        long nowTimestamp = System.currentTimeMillis();
        int difftime = ((Long)(nextTwoTimestamp - nowTimestamp)).intValue();
        return difftime/1000;
    }
}
