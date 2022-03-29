package com.kaisengao.base.loadpage.load.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;

/**
 * @ClassName: BaseLoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 17:12
 * @Description: Load状态父类
 */
public abstract class BaseLoad implements ILoad, View.OnAttachStateChangeListener {

    private Context mContext;

    private View mRootView;

    private Object mTarget;

    private OnLoadViewClickListener mLoadViewClickListener;

    /**
     * Init
     *
     * @param context               context
     * @param target                绑定的目标
     * @param loadViewClickListener loadViewClickListener
     */
    @Override
    public void init(Context context, Object target, OnLoadViewClickListener loadViewClickListener) {
        this.mContext = context;
        this.mTarget = target;
        this.mLoadViewClickListener = loadViewClickListener;
        // Init RootView
        this.initRootView();
    }

    /**
     * Init RootView
     */
    private void initRootView() {
        this.mRootView = View.inflate(mContext, getContentLayoutId(), null);
        this.mRootView.addOnAttachStateChangeListener(this);
        this.mRootView.setOnClickListener(view -> {
            if (onClickEvent()) {
                return;
            }
            this.onLoadClick();
        });
    }

    /**
     * 获取RootView
     *
     * @return RootView
     */
    @Override
    public View getRootView() {
        return mRootView;
    }

    /**
     * findViewById
     *
     * @param id ViewId
     * @return View
     */
    protected final <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    /**
     * 点击事件
     */
    protected void onLoadClick() {
        if (mLoadViewClickListener != null) {
            this.mLoadViewClickListener.onLoadClick(mTarget, this);
        }
    }

    /**
     * 点击事件
     *
     * @return /p
     * False 执行RootView的事件
     * True  执行ChildView的事件
     */
    protected boolean onClickEvent() {
        return false;
    }

    /**
     * 界面布局的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 可视界面的时候调用
     *
     * @param v View
     */
    @Override
    public void onViewAttachedToWindow(View v) {
        // 初始Context
        this.mContext = v.getContext();
        // View 与页面视图绑定
        this.onViewBind();
    }

    /**
     * 移除界面的时候调用
     *
     * @param v View
     */
    @Override
    public void onViewDetachedFromWindow(View v) {
        // 释放资源
        this.mContext = null;
        // View 与页面视图解绑
        this.onViewUnBind();
    }

    /**
     * View与页面绑定
     */
    protected void onViewBind() {
    }

    /**
     * View与页面解绑
     */
    protected void onViewUnBind() {
    }
}