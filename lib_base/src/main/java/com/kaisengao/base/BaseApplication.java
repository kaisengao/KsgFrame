package com.kaisengao.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kaisengao.base.configure.ActivityManager;


/**
 * @ClassName: BaseApplication
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:27
 * @Description: Application
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static BaseApplication sApplication;

    public static BaseApplication getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
        // Fresco
        Fresco.initialize(this);
        // 监控Activity的生命周期
        this.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        ActivityManager.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        ActivityManager.getInstance().setCurrActivity(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ActivityManager.getInstance().removeActivity(activity);
    }
}
