package com.kaisengao.mvvm.binding.adapter.loadsir;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.kaisengao.mvvm.emun.LoadState;
import com.kaisengao.retrofit.factory.LoadSirFactory;

/**
 * @ClassName: ViewAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/18 14:36
 * @Description: 绑定 LoadSir
 */
public class LoadSirAdapter {

    @SuppressLint("SwitchIntDef")
    @BindingAdapter(value = {"loadState", "loadMessage", "loadColor", "loadBgColor"}, requireAll = false)
    public static void loadRegister(final View target,
                                    final @LoadState int loadState,
                                    final String loadMessage,
                                    final ColorDrawable loadColor,
                                    final ColorDrawable loadBgColor) {
        // 过滤 初始化..
        if (loadState == LoadState.INITIAL) {
            return;
        }
        LoadSirFactory loadSirFactory = LoadSirFactory.getInstance();
        // 注册LoadSir
        loadSirFactory.register(target.getContext(), target);
        // 区分状态
        switch (loadState) {
            case LoadState.LOADING:
                loadSirFactory.showLoading(target.getContext(), target, loadMessage, loadColor, loadBgColor);
                break;
            case LoadState.SUCCESS:
                loadSirFactory.showSuccess(target.getContext(), target);
                break;
            case LoadState.ERROR:
                loadSirFactory.showError(target.getContext(), target, loadMessage, loadColor, loadBgColor);
                break;
            default:
                break;
        }
    }
}
