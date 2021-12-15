package com.kaisengao.base.loadpage.widget;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;
import com.kaisengao.base.loadpage.load.ILoad;
import com.kaisengao.base.loadpage.load.OriginalViewLoad;

import java.util.HashMap;

/**
 * @ClassName: LoadContainer
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 17:23
 * @Description: Load容器视图
 */
public class LoadContainer extends FrameLayout implements View.OnAttachStateChangeListener {

    private final String TAG = getClass().getSimpleName();

    private Object mTarget;

    private HashMap<String, ILoad> mLoadMaps;

    private ILoad mCurLoadView;

    private Class<? extends ILoad> mDefaultLoad = OriginalViewLoad.class;

    private OnLoadViewClickListener mLoadViewClickListener;

    public LoadContainer(@NonNull Context context) {
        super(context);
    }

    public LoadContainer(@NonNull Context context, Object target, View originalView) {
        super(context);
        this.mTarget = target;
        // Init
        this.init(originalView);
    }

    /**
     * Init
     */
    private void init(View originalView) {
        // 缓存集合
        this.mLoadMaps = new HashMap<>();
        // View的依附变化回调
        this.addOnAttachStateChangeListener(this);
        // 原视图
        this.mLoadMaps.put(OriginalViewLoad.class.getSimpleName(),
                new OriginalViewLoad(originalView, mTarget));
        // 默认显示默认视图
        this.showLoad(mDefaultLoad);
    }

    /**
     * LoadView 点击事件
     *
     * @param loadViewClickListener clickListener
     */
    public void setLoadViewClickListener(OnLoadViewClickListener loadViewClickListener) {
        this.mLoadViewClickListener = loadViewClickListener;
    }

    /**
     * 默认Load
     *
     * @param defaultLoad Class
     */
    public void setDefaultLoad(final Class<? extends ILoad> defaultLoad) {
        if (defaultLoad == null) {
            return;
        }
        this.mDefaultLoad = defaultLoad;
        // 显示Load
        this.showLoad(defaultLoad);
    }

    /**
     * 显示原视图
     */
    public void showOriginalView() {
        this.showLoad(OriginalViewLoad.class);
    }

    /**
     * 显示Load
     *
     * @param loadView {@link ILoad} 状态类型
     */
    public void showLoad(final Class<? extends ILoad> loadView) {
        try {
            ILoad load;
            String simpleName = loadView.getSimpleName();
            if (mLoadMaps.containsKey(simpleName)) {
                load = mLoadMaps.get(simpleName);
            } else {
                load = loadView.newInstance();
                this.mLoadMaps.put(simpleName, load);
            }
            if (load != null) {
                if (isMainThread()) {
                    // 显示 ILoad View
                    this.showLoadView(load);
                } else {
                    // 主线程 显示 ILoad View
                    this.post(() -> showLoadView(load));
                }
            } else {
                Log.i(TAG, "LoadView Null");
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            Log.i(TAG, "LoadView catch = " + e.getMessage());
        }
    }

    /**
     * 显示 LoadView
     *
     * @param load {@link ILoad} 状态类型
     */
    private void showLoadView(ILoad load) {
        if (mCurLoadView != null && mCurLoadView == load) {
            return;
        }
        this.mCurLoadView = load;
        // 移除全部View
        this.removeAllViews();
        // 添加LoadView
        this.addView(load.getRootView(getContext(), mTarget, mLoadViewClickListener));
    }

    /**
     * 获取当前显示 LoadView
     *
     * @return {@link ILoad} 状态类型
     */
    public ILoad getCurLoadView() {
        return mCurLoadView;
    }

    /**
     * 可视界面的时候调用
     *
     * @param v View
     */
    @Override
    public void onViewAttachedToWindow(View v) {

    }

    /**
     * 移除界面的时候调用
     *
     * @param v View
     */
    @Override
    public void onViewDetachedFromWindow(View v) {
        this.mTarget = null;
        // 释放资源
        this.mLoadMaps.clear();
        this.mLoadMaps = null;
        // View的依附变化回调
        this.removeOnAttachStateChangeListener(this);
    }

    /**
     * 是否是主线程
     *
     * @return boolean
     */
    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}