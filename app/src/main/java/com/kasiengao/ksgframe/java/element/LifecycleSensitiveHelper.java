package com.kasiengao.ksgframe.java.element;

import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.kasiengao.base.util.CommonUtil;

/**
 * @ClassName: LifecycleSensitiveHelper
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 16:57
 * @Description: 生命周期敏感帮助类
 */
public class LifecycleSensitiveHelper {

    public static Lifecycle registerLifecycle(Context context, MyLifecycleObserver observer) {
        return registerLifecycle(CommonUtil.scanForActivity(context), observer);
    }

    public static Lifecycle registerLifecycle(AppCompatActivity activity, MyLifecycleObserver observer) {
        return registerLifecycle(activity.getLifecycle(), observer);
    }

    public static Lifecycle registerLifecycle(Lifecycle lifecycle, MyLifecycleObserver observer) {
        lifecycle.addObserver(observer);
        return lifecycle;
    }

    public static void unRegisterLifecycle(Lifecycle lifecycle, MyLifecycleObserver observer) {
        lifecycle.removeObserver(observer);
    }

    public static Lifecycle.State getCurrentState(Lifecycle lifecycle) {
        return lifecycle.getCurrentState();
    }
}
