package com.gogovan.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * 工具类
 * Created by Arthur on 2018/11/10.
 */
public class WUtils<T> {

    private static StringBuilder formaterBuilder = new StringBuilder();

    private static Formatter formatter = new Formatter(formaterBuilder,Locale.getDefault());


    /**
     *  将时间转换成显示的格式
     * @param totalSeconds
     * @return 00:00:00
     */
    public static String stringFromTime(long totalSeconds) {

        long hours = (totalSeconds/3600);
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds/60)%60;
        formaterBuilder.setLength(0);

//        if(hours >0) {
//
//        }else {
//            return formatter.format("%02d:%02d",minutes,seconds).toString();
//
//        }

        return formatter.format("%02d:%02d:%02d",hours,minutes,seconds).toString();


    }


}
