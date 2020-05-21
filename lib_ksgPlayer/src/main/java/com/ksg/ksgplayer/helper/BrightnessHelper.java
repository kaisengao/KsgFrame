package com.ksg.ksgplayer.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author kaisengao
 * @create: 2019/3/8 13:48
 * @describe: 屏幕亮度 只是调节App层面的亮度，而非系统层。
 */
public class BrightnessHelper {

    /**
     * 以0-100为调节范围的，亮度调节步进值。
     */
    private static int STEP_LIGHT_100 = 2;

    /**
     * 设置亮度步进值
     *
     * @param stepValue 0 - 100
     */
    public static void setStepLight100(int stepValue) {
        STEP_LIGHT_100 = stepValue;
    }

    public static int addLight100(Activity activity) {
        int a = getAppLight100(activity) + STEP_LIGHT_100;
        a = a >= 100 ? 100 : a;
        return setAppLight100(activity, a);
    }

    public static int subLight100(Activity activity) {
        int a = getAppLight100(activity) - STEP_LIGHT_100;
        a = a <= 0 ? 0 : a;
        return setAppLight100(activity, a);
    }

    /**
     * 以1-100为散列，设置App层面的亮度
     *
     * @param activity activity
     * @param paramInt 0 - 100
     */
    public static int setAppLight100(Activity activity, int paramInt) {
        paramInt = paramInt <= 0 ? 0 : paramInt;
        paramInt = paramInt >= 100 ? 100 : paramInt;
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = paramInt * 0.01f;
        localWindow.setAttributes(localLayoutParams);
        return paramInt;
    }

    /**
     * 以1-100为散列，获取App层面的亮度
     *
     * @param activity activity
     * @return 1-100
     */
    public static int getAppLight100(Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        int result = (int) (localLayoutParams.screenBrightness * 100);
        result = result < 0 ? getScreenBrightness100(activity) : result;
        return result;
    }

    /**
     * 获取当前屏幕的亮度
     *
     * @param context context
     * @return int
     */
    private static int getScreenBrightness100(Context context) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = context.getContentResolver();
        try {
            //这里获取到的是：1-225的亮度
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) (nowBrightnessValue * 1.0 / 2.25);
    }
}