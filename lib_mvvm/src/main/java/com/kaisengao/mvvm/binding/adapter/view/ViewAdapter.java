package com.kaisengao.mvvm.binding.adapter.view;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;

import com.kaisengao.base.factory.AppFactory;
import com.kaisengao.base.util.StatusBarUtil;

/**
 * @ClassName: ViewAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/28 11:36
 * @Description:
 */
public class ViewAdapter {

    @BindingAdapter("setViewHeight")
    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.requestLayout();
    }

    @BindingAdapter("setPaddingSmart")
    public static void setPaddingSmart(View view, int padding) {
        StatusBarUtil.setStatusBarPadding(AppFactory.application(), view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("setInterceptTouch")
    public static void setInterceptTouch(View view, boolean intercept) {
        view.setOnTouchListener((v, event) -> intercept);
    }
}
