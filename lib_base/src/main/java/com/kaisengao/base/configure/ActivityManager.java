package com.kaisengao.base.configure;

import android.app.Activity;

import java.lang.ref.SoftReference;
import java.util.Stack;

/**
 * @ClassName: ActivityManager
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:29
 * @Description:
 */
public class ActivityManager {

    private static ActivityManager sInstance;

    private Stack<Activity> mActivityStack = new Stack<>();

    private SoftReference<Activity> mCurrActivity;

    public static ActivityManager getInstance() {
        if (sInstance == null) {
            synchronized (ActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new ActivityManager();
                }
            }
        }
        return sInstance;
    }

    private ActivityManager() {
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            this.mActivityStack = new Stack<>();
        }
        this.mActivityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            this.mActivityStack.remove(activity);
        }
    }

    /**
     * 是否有activity
     */
    public boolean isActivity() {
        if (mActivityStack != null) {
            return !mActivityStack.isEmpty();
        }
        return false;
    }

    /**
     * 获取指定的Activity
     *
     * @param cls cls
     */
    public Activity getActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 设置当前Activity
     *
     * @param currActivity currActivity
     */
    public void setCurrActivity(Activity currActivity) {
        if (mCurrActivity != null) {
            this.mCurrActivity.clear();
            this.mCurrActivity = null;
        }
        this.mCurrActivity = new SoftReference<>(currActivity);
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        if (mCurrActivity == null || mCurrActivity.get() == null) {
            return mActivityStack.lastElement();
        }
        return mCurrActivity.get();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        this.finishActivity(mActivityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     */
    private void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                this.finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                this.finishActivity(mActivityStack.get(i));
            }
        }
        this.mActivityStack.clear();
        this.mCurrActivity.clear();
    }
}