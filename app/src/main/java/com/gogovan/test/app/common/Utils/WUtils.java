package com.gogovan.test.app.common.Utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Arthur on 2018/11/10.
 * tools
 */
public class WUtils {

    private static StringBuilder formaterBuilder = new StringBuilder();

    private static Formatter formatter = new Formatter(formaterBuilder,Locale.getDefault());


    /**
     *
     * @param totalSeconds
     * @return
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
