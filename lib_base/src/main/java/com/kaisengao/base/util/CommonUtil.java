package com.kaisengao.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @ClassName: CommonUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:35
 * @Description:
 */
public class CommonUtil {

    /**
     * Get PermissionActivity from context object
     *
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    public static AppCompatActivity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }
}
