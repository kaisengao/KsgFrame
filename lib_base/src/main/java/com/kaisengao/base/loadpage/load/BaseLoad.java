package com.kaisengao.base.loadpage.load;

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
public abstract class BaseLoad implements View.OnAttachStateChangeListener {

    private Context mContext;

    private View rootView;

    private OnLoadViewClickListener mLoadViewClickListener;

    public BaseLoad() {
    }

    public BaseLoad(View rootView, OnLoadViewClickListener loadViewClickListener) {
        this.rootView = rootView;
        this.mLoadViewClickListener = loadViewClickListener;
    }

    public View getRootView(Context context, OnLoadViewClickListener loadViewClickListener) {
        this.mContext = context;
        this.mLoadViewClickListener = loadViewClickListener;
        if (rootView == null) {
            this.rootView = View.inflate(mContext, getContentLayoutId(), null);
            this.rootView.addOnAttachStateChangeListener(this);
        }
        if (rootView != null && mLoadViewClickListener != null) {
            rootView.setOnClickListener(view -> {
                if (onClickEvent()) {
                    return;
                }
                this.mLoadViewClickListener.onLoadClick(view);
            });
        }
        return rootView;
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
        // View 与页面视图绑定
        this.onModeViewBind();
    }

    /**
     * 移除界面的时候调用
     *
     * @param v View
     */
    @Override
    public void onViewDetachedFromWindow(View v) {
        this.rootView.removeOnAttachStateChangeListener(this);
        // View 与页面视图解绑
        this.onModeViewUnBind();
        // 释放资源 避免长时间引用
        this.mContext = null;
    }

    /**
     * View与页面绑定
     */
    protected void onModeViewBind() {
    }

    /**
     * View与页面解绑
     */
    protected void onModeViewUnBind() {
    }
}