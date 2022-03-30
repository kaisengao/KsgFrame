package com.kaisengao.base.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: TimeUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/27 14:35
 * @Description: 时间转换
 */
public class TimeUtil {

    private static final long SECONDS_ONE_HOUR = 60 * 60;

    public static final String TIME_FORMAT_01 = "%02d:%02d";
    public static final String TIME_FORMAT_02 = "%02d:%02d:%02d";

    private static final ThreadLocal<Map<String, SimpleDateFormat>> SDF_THREAD_LOCAL
            = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<>();
        }
    };

    private static SimpleDateFormat getDefaultFormat() {
        return getSafeDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getSafeDateFormat(String pattern) {
        Map<String, SimpleDateFormat> sdfMap = SDF_THREAD_LOCAL.get();
        SimpleDateFormat simpleDateFormat = sdfMap.get(pattern);
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern);
            sdfMap.put(pattern, simpleDateFormat);
        }
        return simpleDateFormat;
    }

    private TimeUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Get CurrentTime
     */
    public static String getCurrentTime() {
        return millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Get CurrentTime
     */
    public static String getCurrentTime(@NonNull final String pattern) {
        return millis2String(System.currentTimeMillis(), pattern);
    }
    /**
     * Milliseconds to the formatted time string.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param millis The milliseconds.
     * @return the formatted time string
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, getDefaultFormat());
    }

    /**
     * Milliseconds to the formatted time string.
     *
     * @param millis  The milliseconds.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the formatted time string
     */
    public static String millis2String(long millis, @NonNull final String pattern) {
        return millis2String(millis, getSafeDateFormat(pattern));
    }

    /**
     * Milliseconds to the formatted time string.
     *
     * @param millis The milliseconds.
     * @param format The format.
     * @return the formatted time string
     */
    public static String millis2String(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }

    /**
     * Formatted time string to the date.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return the date
     */
    public static Date string2Date(final String time) {
        return string2Date(time, getDefaultFormat());
    }

    /**
     * Formatted time string to the date.
     *
     * @param time    The formatted time string.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the date
     */
    public static Date string2Date(final String time, @NonNull final String pattern) {
        return string2Date(time, getSafeDateFormat(pattern));
    }

    /**
     * Formatted time string to the date.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return the date
     */
    public static Date string2Date(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * Date to the formatted time string.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param date The date.
     * @return the formatted time string
     */
    public static String date2String(final Date date) {
        return date2String(date, getDefaultFormat());
    }

    /**
     * Date to the formatted time string.
     *
     * @param date    The date.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the formatted time string
     */
    public static String date2String(final Date date, @NonNull final String pattern) {
        return getSafeDateFormat(pattern).format(date);
    }

    /**
     * Date to the formatted time string.
     *
     * @param date   The date.
     * @param format The format.
     * @return the formatted time string
     */
    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }

    /**
     * Date to the milliseconds.
     *
     * @param date The date.
     * @return the milliseconds
     */
    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    /**
     * Milliseconds to the date.
     *
     * @param millis The milliseconds.
     * @return the date
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    /**
     * 计算两个时间的时间差
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return long
     */
    public static long timeDiff(Date startDate, Date endDate) {
        // 时间差
        return endDate.getTime() - startDate.getTime();
    }

    /**
     * 转换时间
     *
     * @param time time
     * @return String
     */
    public static String getTimeFormatText(long time) {
        // 天
        long days = time / (1000 * 60 * 60 * 24);
        // 时
        long hours = (time - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        // 分
        long minutes = (time - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        // 秒
        long second = (time / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);
        // 拼接
        String timeStr = "";
        if (days > 0) {
            timeStr += days + "天";
        }
        if (hours > 0) {
            timeStr += hours + "小时";
        }
        if (minutes > 0) {
            timeStr += minutes + (second > 0 ? "分" : "分钟");
        }
        if (second > 0) {
            if (second < 10) {
                timeStr += 0;
            }
            timeStr += second + "秒";
        }
        return timeStr;
    }

    public static String getTimeFormat1(long timeMs) {
        return getTimeFormatText(TIME_FORMAT_01, timeMs);
    }

    public static String getTimeFormat2(long timeMs) {
        return getTimeFormatText(TIME_FORMAT_02, timeMs);
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

    public static String getTimeFormatText(String format, long time) {
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
