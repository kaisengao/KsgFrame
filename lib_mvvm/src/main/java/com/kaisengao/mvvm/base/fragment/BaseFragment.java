package com.kaisengao.mvvm.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * @ClassName: BaseFragment
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/22 17:06
 * @Description: MVVM BaseFragment
 */
public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment {

    protected boolean isLazyLoad;

    protected DB mBinding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 初始化相关参数
        this.initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, getContentLayoutId(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initWidget();
        this.initData();
    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     */
    protected void initArgs(Bundle bundle) {

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

    @Override
    public void onResume() {
        super.onResume();
        // 懒加载
        if (!isLazyLoad) {
            this.lazyLoad();
            this.isLazyLoad = true;
        }
    }

    /**
     * onBackPressed
     */
    public Boolean onBackPressed() {
        return false;
    }

    /**
     * LayoutId
     */
    protected abstract int getContentLayoutId();

    /**
     * 标题
     */
    public abstract String getTitle();

    /**
     * 懒加载
     */
    protected abstract void lazyLoad();

}
