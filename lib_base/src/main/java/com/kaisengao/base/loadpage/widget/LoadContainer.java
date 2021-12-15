package com.kaisengao.base.loadpage.widget;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.kaisengao.base.loadpage.helper.TargetHelper;
import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;
import com.kaisengao.base.loadpage.load.base.ILoad;

import java.util.HashMap;

/**
 * @ClassName: LoadContainer
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 17:23
 * @Description: Load容器视图
 */
public class LoadContainer extends FrameLayout {

    private final String TAG = getClass().getSimpleName();

    private int mTargetIndex;

    private Object mTarget;

    private ViewGroup mTargetParent;

    private View mTargetView;

    private HashMap<String, ILoad> mLoadMaps;

    private ILoad mCurLoadView;

    private OnLoadViewClickListener mLoadViewClickListener;

    public LoadContainer(@NonNull Context context) {
        super(context);
    }

    public LoadContainer(@NonNull Object target, @NonNull ViewGroup targetParent) {
        super(targetParent.getContext());
        this.mTarget = target;
        this.mTargetParent = targetParent;
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        // LoadView缓存
        this.mLoadMaps = new HashMap<>();
        // 获取原视图View在父view的坐标
        this.mTargetIndex = TargetHelper.getTargetIndexOfChild(mTarget, mTargetParent);
        // 获取绑定目标的View (原视图View)
        this.mTargetView = mTargetParent.getChildAt(mTargetIndex);
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
     * 显示LoadView
     *
     * @param loadView {@link ILoad} 状态类型
     */
    public void showLoadView(final Class<? extends ILoad> loadView) {
        // 替换原View
        if (getParent() == null) {
            // 克隆Id
            this.setId(mTargetView.getId());
            // 将目标从父View中移除
            this.mTargetParent.removeViewAt(mTargetIndex);
            // 将容器添加到父View中
            this.mTargetParent.addView(this, mTargetIndex, mTargetView.getLayoutParams());
        }
        try {
            ILoad load;
            String simpleName = loadView.getSimpleName();
            if (mLoadMaps.containsKey(simpleName)) {
                load = mLoadMaps.get(simpleName);
            } else {
                load = loadView.newInstance();
                load.init(getContext(), mTarget, mLoadViewClickListener);
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
        this.addView(load.getRootView());
    }

    /**
     * 显示原视图
     */
    public void showOriginalView() {
        // 移除Id
        this.setId(NO_ID);
        // 将Load容器视图从父View中移除
        this.mTargetParent.removeViewAt(mTargetIndex);
        // 将原视图View放回原位
        this.mTargetParent.addView(mTargetView, mTargetIndex);
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
     * 是否是主线程
     *
     * @return boolean
     */
    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}