package com.kaisengao.mvvm.base.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.kaisengao.base.util.StatusBarUtil;

/**
 * @ClassName: BaseActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/16 16:46
 * @Description: MVVM BaseActivity
 */
public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    protected DB mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initWindow();
        this.initArgs(getIntent().getExtras());
        this.initDB();
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
     * 初始化 DataBinding
     */
    private void initDB() {
        this.mBinding = DataBindingUtil.setContentView(this, getContentLayoutId());
        this.mBinding.setLifecycleOwner(this);
    }

    /**
     * 初始化窗口
     */
    protected void initWindow() {
        // 设置noTitle
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
        StatusBarUtil.StatusBarDarkMode(this);
        StatusBarUtil.transparencyBar(this, Color.WHITE);
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
}
