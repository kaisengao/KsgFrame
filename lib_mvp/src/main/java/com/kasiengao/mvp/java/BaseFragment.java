package com.kasiengao.mvp.java;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @ClassName: BaseFragment
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 16:07
 * @Description: Fragment 的基类 Base层
 */
public abstract class BaseFragment extends Fragment {

    private View mContentView = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 初始化相关参数
        this.initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContentView = inflater.inflate(getContentLayoutId(), container, false);
        // 初始化控件
        this.initWidget();
        return this.mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化数据
        this.initData();
    }

    protected View getContentView() {
        return mContentView;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 初始化相关参数
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
}
