package com.kaisengao.base.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @ClassName: StatusBarUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/2 10:28
 * @Description: 系统状态栏/导航栏
 */
public final class StatusBarUtil {

    private static final int PRIMARY = -1;

    private static final int MIUI = 1;

    private static final int FLYME = 2;

    /**
     * 全透明状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void transparencyBar(Activity activity) {
        transparencyBar(activity, 0);
    }

    /**
     * 设置颜色状态栏
     *
     * @param activity 需要设置的activity
     * @param color    颜色
     */
    public static void transparencyBar(Activity activity, int color) {
        int barColor = color == 0 ? Color.TRANSPARENT : color;
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().setStatusBarColor(barColor);
        int systemUiVisibility =
                activity.getWindow().getDecorView().getSystemUiVisibility()
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUI、Flyme和6.0以上版本其他Android
     *
     * @param activity 需要设置的activity
     */
    public static void StatusBarDarkMode(Activity activity) {
        switch (getSystemType()) {
            case MIUI:
                MIUISetStatusBarLightMode(activity.getWindow(), true);
                break;
            case FLYME:
                FlymeSetStatusBarLightMode(activity.getWindow(), true);
                break;
            case PRIMARY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
                }
            default:
                break;
        }
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     *
     * @param activity 需要设置的activity
     */
    public static void StatusBarLightMode(Activity activity) {
        switch (getSystemType()) {
            case MIUI:
                MIUISetStatusBarLightMode(activity.getWindow(), false);
                break;
            case FLYME:
                FlymeSetStatusBarLightMode(activity.getWindow(), false);
                break;
            case PRIMARY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
                }
            default:
                break;
        }
    }

    /**
     * 获取系统类型
     *
     * @return int
     */
    private static int getSystemType() {
        if (isMIUI6Later()) {
            return MIUI;
        } else if (isFlyme4Later()) {
            return FLYME;
        } else {
            return PRIMARY;
        }
    }

    /**
     * 判断是否Flyme4以上
     */
    private static boolean isFlyme4Later() {
        String regex = "Flyme OS [4|5]";
        return Build.FINGERPRINT.contains("Flyme_OS_4")
                || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
    }

    /**
     * 判断是否为MIUI6以上
     */
    private static boolean isMIUI6Later() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method mtd = clz.getMethod("getView", String.class);
            String val = (String) mtd.invoke(null, "ro.miui.ui.version.name");
            if (!TextUtils.isEmpty(val)) {
                val = val.replaceAll("[vV]", "");
                int version = Integer.parseInt(val);
                return version >= 6;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     */
    private static void FlymeSetStatusBarLightMode(Window window, boolean dark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUI V6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     */
    private static void MIUISetStatusBarLightMode(Window window, boolean dark) {
        if (window != null) {
            Class<?> clazz = window.getClass();
            try {
                int darkModeFlag;
                @SuppressLint("PrivateApi")
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    // 状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    // 清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
     */
    public static void setStatusBarPadding(Context context, View view) {
        int statusBarHeight = getStatusBarHeight(context);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.height > 0) {
            params.height += statusBarHeight;
        }
        view.setLayoutParams(params);
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + statusBarHeight,
                view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 24;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, result, Resources.getSystem().getDisplayMetrics());
        }
        return result;
    }
}
