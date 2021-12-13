package com.kasiengao.ksgframe.common.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kaisengao.base.util.CommonUtil;
import com.kaisengao.base.util.StatusBarUtil;

/**
 * @ClassName: SystemUiUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/28 17:20
 * @Description: 系统Ui
 */
public class SystemUiUtil {

    /**
     * 恢复默认
     *
     * @param context context
     */
    public static void recoverySystemUI(Context context) {
        Activity activity = CommonUtil.scanForActivity(context);
        Window window = activity.getWindow();
        // 显示状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 恢复初始默认值
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        // 默认 全透明状态栏
        StatusBarUtil.transparencyBar(activity);
    }

    /**
     * 隐藏 导航栏 与 状态栏
     *
     * @param context context
     */
    public static void hideVideoSystemUI(Context context) {
        Activity activity = CommonUtil.scanForActivity(context);
        Window window = activity.getWindow();
        // 隐藏状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int uiFlags =
                // 隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        // 视图延伸至导航栏区域，导航栏上浮于视图之上
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        /*
         * SYSTEM_UI_FLAG_IMMERSIVE 正如前面所说的 SYSTEM_UI_FLAG_HIDE_NAVIGATION 和 SYSTEM_UI_FLAG_FULLSCREEN在用户与屏幕有任何交互时，都会被清除。
         * 过后如需隐藏目的，又得重新设置。而此标签正是防止这种情况而加入，设置此标签后，只有从屏幕上方下滑，或者从屏幕下方上滑时才会执行清除，其他普通交互不会变化。
         *
         * SYSTEM_UI_FLAG_IMMERSIVE_STICKY 该标签与SYSTEM_UI_FLAG_IMMERSIVE作用差不多。
         * 只是该标签会让SYSTEM_UI_FLAG_HIDE_NAVIGATION和SYSTEM_UI_FLAG_FULLSCREEN 标签被短暂清除，而不是永久，一会儿后又会自动恢复。
         */

        window.getDecorView().setSystemUiVisibility(uiFlags);
    }
}
