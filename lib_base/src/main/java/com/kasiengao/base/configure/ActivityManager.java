package com.kasiengao.base.configure;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import java.util.Stack;

/**
 * @ClassName: ActivityManager
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:29
 * @Description: Activity管理
 */
public class ActivityManager {

    private Stack<Activity> mActivityStack = new Stack<>();

    private Stack<Fragment> mFragmentStack = new Stack<>();

    private static ActivityManager sInstance;

    private ActivityManager() {
    }

    /**
     * 单例模式
     *
     * @return ActivityManager
     */
    public static ActivityManager getAppManager() {
        if (sInstance == null) {
            synchronized (ActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new ActivityManager();
                }
            }
        }
        return sInstance;
    }

    public Stack<Activity> getActivityStack() {
        return mActivityStack;
    }

    public Stack<Fragment> getFragmentStack() {
        return mFragmentStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
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
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
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
                finishActivity(activity);
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
                finishActivity(mActivityStack.get(i));
            }
        }
        mActivityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
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
     * 添加Fragment到堆栈
     */
    public void addFragment(Fragment fragment) {
        if (mFragmentStack == null) {
            mFragmentStack = new Stack<Fragment>();
        }
        mFragmentStack.add(fragment);
    }

    /**
     * 移除指定的Fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            mFragmentStack.remove(fragment);
        }
    }


    /**
     * 是否有Fragment
     */
    public boolean isFragment() {
        if (mFragmentStack != null) {
            return !mFragmentStack.isEmpty();
        }
        return false;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Fragment currentFragment() {
        if (mFragmentStack != null) {
            return mFragmentStack.lastElement();
        }
        return null;
    }


    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            mActivityStack.clear();
            e.printStackTrace();
        }
    }
}