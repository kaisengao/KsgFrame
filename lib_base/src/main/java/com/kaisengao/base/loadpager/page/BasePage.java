package com.kaisengao.base.loadpager.page;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;

/**
 * @ClassName: BasePage
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 17:12
 * @Description: Page状态父类
 */
public abstract class BasePage implements View.OnAttachStateChangeListener {

    private Context mContext;

    private View rootView;

    public BasePage() {
    }

    public BasePage(Context context) {
        this.mContext = context;
    }

    private void init() {
        if (rootView == null) {
            this.rootView = View.inflate(mContext, getContentLayoutId(), null);
        }
        this.rootView.addOnAttachStateChangeListener(this);
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
        // View 与页面视图解绑
        this.onModeViewUnBind();
        // 释放资源 避免长时间引用
        this.mContext = null;
    }

    /**
     * View与页面绑定
     */
    public abstract void onModeViewBind();

    /**
     * View与页面解绑
     */
    public abstract void onModeViewUnBind();

}