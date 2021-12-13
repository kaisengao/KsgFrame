package com.kasiengao.mvp.java;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @ClassName: BaseActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 13:58
 * @Description: Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initWindow();
        this.initArgs(getIntent().getExtras());
        this.setContentView(getContentLayoutId());
        this.initBefore();
        this.initWidget();
        this.initData();
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 初始化窗口
     */
    protected void initWindow() {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化控件
     */
    protected void initWidget() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 返回LoadSir需要覆盖的布局
     *
     * @return 默认返回true表示显示覆盖 toolbar以下的布局
     */
    protected Object getInflate() {
        return this;
    }
}
