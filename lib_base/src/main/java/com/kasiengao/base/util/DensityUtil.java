package com.kasiengao.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class DensityUtil {

    public static float getHeightInPx(Context context) {
        return (float) context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getWidthInPx(Context context) {
        return (float) context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return px2dip(context, height);
    }

    public static int getWidthInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return px2dip(context, height);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public static int dp2px(Float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dp2px(Context context, int dpVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);
    }

    public static int sp2px(Context context, int spVal) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spVal * fontScale + 0.5f);
    }

    public static int[] getScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (context instanceof Activity) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        } else {
            displayMetrics = context.getResources().getDisplayMetrics();
        }
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    /**
     * 使用dp单位
     *
     * @param context
     * @param value
     * @return
     */
    public static int getDp(Context context, float value) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics()));
    }

    public static Float getFloat(Float value, Float minValue, Float maxValue) {
        return Math.min(maxValue, Math.max(minValue, value));
    }
}
