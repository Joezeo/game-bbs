package com.joezeo.joefgame.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private TimeUtils() {
    }

    /**
     * 获取当前日期的字符串 格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDateStr(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(System.currentTimeMillis()));
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
     * 获取当前时间与下一个凌晨4点之间的时间差
     * 单位秒
     */
    public static int getDifftimeFromNextZero() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String preDateStr = dateFormat.format(new Date(System.currentTimeMillis())); // 当天的年月日，如：20200210
        String nextTwoStr = (Integer.parseInt(preDateStr) + 1) + "04";
        dateFormat = new SimpleDateFormat("yyyyMMddhh");
        Date nextTwoDate = dateFormat.parse(nextTwoStr);

        long nextTwoTimestamp = nextTwoDate.getTime();
        long nowTimestamp = System.currentTimeMillis();
        int difftime = ((Long) (nextTwoTimestamp - nowTimestamp)).intValue();
        return difftime / 1000;
    }

    /**
     * 将时间戳转化成为int类型
     * 如：20200215
     */
    public static Integer timeToInt(Long gmtCreate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String preDateStr = dateFormat.format(new Date(gmtCreate)); // 转换时间戳，如：20200215
        return Integer.parseInt(preDateStr);
    }

    /**
     * 把时间从20200215格式转换成2020-02-15格式
     */
    public static String tansferInt(int i) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = dateFormat.parse(String.valueOf(i));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String target = format.format(date);
        return target;
    }

    /**
     * @param date 传入以int形式表示的日期如（20200301）
     * @return date加上一天后的日期的int表示
     */
    public static Integer dateAddOneDay(Integer date) {
        String yearStr = date.toString().substring(0, 4);
        Integer year = Integer.parseInt(yearStr);
        String monthStr = date.toString().substring(4, 6);
        Integer month = Integer.parseInt(monthStr);
        String dayStr = date.toString().substring(6);
        Integer day = Integer.parseInt(dayStr);

        if (month == 1 || month == 3 || month == 5
                || month == 7 || month == 8 || month == 10 || month == 12) { // 大月份
            if (day + 1 > 31) {
                dayStr = "01";
                month += 1;
                if (month > 12) {
                    monthStr = "01";
                    year += 1;
                } else if (month > 10) {
                    monthStr = month.toString();
                } else {
                    monthStr = "0" + month;
                }
                return Integer.parseInt(year + monthStr + dayStr);
            } else {
                return date + 1;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) { // 小月份
            if (day + 1 > 30) {
                dayStr = "01";
                month += 1;
                if (month > 12) {
                    monthStr = "01";
                    year += 1;
                } else if (month > 10) {
                    monthStr = month.toString();
                } else {
                    monthStr = "0" + month;
                }
                return Integer.parseInt(year + monthStr + dayStr);
            } else {
                return date + 1;
            }
        } else if (month == 2) {
            Integer baseDay;
            // 判断平年与闰年
            if (year % 4 == 0 && year % 100 != 0) { // 闰年
                baseDay = 29;
            } else if (year % 100 == 0) {
                if (year % 400 == 0) { // 闰年
                    baseDay = 29;
                } else { //平年
                    baseDay = 28;
                }
            } else { // 平年
                baseDay = 28;
            }
            if (day + 1 > baseDay) {
                dayStr = "01";
                month += 1;
                if (month > 12) {
                    monthStr = "01";
                    year += 1;
                } else if (month > 10) {
                    monthStr = month.toString();
                } else {
                    monthStr = "0" + month;
                }
                return Integer.parseInt(year + monthStr + dayStr);
            } else {
                return date + 1;
            }
        }

        return date + 1;
    }
}
