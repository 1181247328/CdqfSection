package com.cdqf.cart_state;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 获取当前年月日时分以及时间
 */
public class CartCalender {
    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @return
     */
    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DATE);
    }

    /**
     * 获取时
     *
     * @return
     */
    public static int getHour() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分
     *
     * @return
     */
    public static int getMinute() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     *
     * @return
     */
    public static int getSecond() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.SECOND);
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     */

    public static String getCurrentTime() {
        return getFormatedDateTime(System.currentTimeMillis());
    }

    private static String getFormatedDateTime(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
