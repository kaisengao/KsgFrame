package com.kaisengao.base.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
     * @param context context
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

    /**
     * 获取 Assets下的Json文件
     *
     * @param context  context
     * @param fileName fileName
     * @return Json
     */
    public static String getAssetsJson(Context context, String fileName) {
        // 将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // 获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            // 通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
