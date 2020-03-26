package com.kasiengao.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

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
    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

}
