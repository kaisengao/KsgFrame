package com.kaisengao.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kaisengao.base.configure.ActivityManager;
import com.kaisengao.base.factory.LoadPageFactory;

/**
 * @ClassName: BaseApplication
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:27
 * @Description: Application
 */
public class BaseApplication extends Application {

    private ActivityLifecycleCallback mLifecycleCallbacks;

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
        this.mLifecycleCallbacks = new ActivityLifecycleCallback();
        this.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    protected void setBackgroundCallback(OnBackgroundCallback onBackgroundCallback) {
        if (mLifecycleCallbacks != null) {
            this.mLifecycleCallbacks.setBackgroundCallback(onBackgroundCallback);
        }
    }

    private static final class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

        // 运行的Activity个数
        private int mLiveActivityCount = 0;
        // 是否处于压后台状态
        private boolean mIsOnBackground = false;
        // 压后台的回调
        private OnBackgroundCallback mOnBackgroundCallback;

        public void setBackgroundCallback(OnBackgroundCallback onBackgroundCallback) {
            this.mOnBackgroundCallback = onBackgroundCallback;
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
            ActivityManager.getInstance().addActivity(activity);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            this.mLiveActivityCount++;
            // 从压后台回来
            if (mIsOnBackground) {
                // 重置后台标识
                this.mIsOnBackground = false;
                // Callback
                if (mOnBackgroundCallback != null) {
                    this.mOnBackgroundCallback.handleOnForeground(activity);
                }
            }
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
            this.mLiveActivityCount--;
            // 处于后台
            if (mLiveActivityCount == 0) {
                // 重置后台标识
                this.mIsOnBackground = true;
                // Callback
                if (mOnBackgroundCallback != null) {
                    this.mOnBackgroundCallback.handleOnBackground(activity);
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            this.mIsOnBackground = false;
            LoadPageFactory.getInstance().onDetachView(activity);
            ActivityManager.getInstance().removeActivity(activity);
        }
    }

    /**
     * 压后台的回调处理
     */
   protected interface OnBackgroundCallback {

        /**
         * 从前台到后台时触发
         */
        void handleOnBackground(Activity activity);

        /**
         * 从后台回到前台时触发
         */
        void handleOnForeground(Activity activity);
    }
}
