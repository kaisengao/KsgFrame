package com.kasiengao.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kasiengao.base.configure.ActivityManager;
import com.kasiengao.base.loadsir.callback.ErrorCallback;
import com.kasiengao.base.loadsir.callback.LoadingCallback;
import com.kasiengao.base.loadsir.core.LoadSir;

/**
 * @ClassName: BaseApplication
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:27
 * @Description: Application
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static BaseApplication sApplication;

    /**
     * 外部获取单例
     *
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        // 管理Activity
        this.registerActivityLifecycleCallbacks(this);
        // 初始化 LoadSir
        this.initLoadSir();
    }

    /**
     * 初始化 LoadSir
     */
    private void initLoadSir() {
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new LoadingCallback())
                .commit();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        ActivityManager.getAppManager().addActivity(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

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
        ActivityManager.getAppManager().removeActivity(activity);
    }
}
