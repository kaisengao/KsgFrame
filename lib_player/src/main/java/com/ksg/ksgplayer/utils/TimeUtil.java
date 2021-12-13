package com.ksg.ksgplayer.utils;

import android.text.TextUtils;

/**
 * @ClassName: TimeUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/7/20 10:51
 * @Description:
 */
public class TimeUtil {

    private static final long SECONDS_ONE_HOUR = 60 * 60;

    public static final String TIME_FORMAT_01 = "%02d:%02d";
    public static final String TIME_FORMAT_02 = "%02d:%02d:%02d";

    public static String getTimeFormat1(long timeMs) {
        return getTime(TIME_FORMAT_01, timeMs);
    }

    public static String getTimeFormat2(long timeMs) {
        return getTime(TIME_FORMAT_02, timeMs);
    }

    public static String getTimeSmartFormat(long timeMs) {
        int totalSeconds = (int) (timeMs / 1000);
        if (totalSeconds >= SECONDS_ONE_HOUR) {
            return getTimeFormat2(timeMs);
        } else {
            return getTimeFormat1(timeMs);
        }
    }

    public static String getFormat(long maxTimeMs) {
        int totalSeconds = (int) (maxTimeMs / 1000);
        if (totalSeconds >= SECONDS_ONE_HOUR) {
            return TIME_FORMAT_02;
        }
        return TIME_FORMAT_01;
    }

    public static String getTime(String format, long time) {
        if (time <= 0)
            time = 0;
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (TIME_FORMAT_01.equals(format)) {
            return String.format(format, minutes, seconds);
        } else if (TIME_FORMAT_02.equals(format)) {
            return String.format(format, hours, minutes, seconds);
        }
        if (TextUtils.isEmpty(format)) {
            format = TIME_FORMAT_01;
        }
        return String.format(format, hours, minutes, seconds);
    }

}
