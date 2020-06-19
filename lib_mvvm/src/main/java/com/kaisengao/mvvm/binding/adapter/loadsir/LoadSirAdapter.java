package com.kaisengao.mvvm.binding.adapter.loadsir;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.kaisengao.retrofit.factory.LoadSirFactory;
import com.kaisengao.retrofit.listener.OnLoadSirReloadListener;
import com.kasiengao.base.loading.LoadState;
import com.kasiengao.base.loading.LoadingState;

/**
 * @ClassName: ViewAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/18 14:36
 * @Description: 绑定 LoadSir
 */
public class LoadSirAdapter {


    @BindingAdapter("loadRegister")
    public static void loadRegister(final View target, final OnLoadSirReloadListener reloadListener) {
        LoadSirFactory loadSirFactory = LoadSirFactory.getInstance();
        // 注册LoadSir
        loadSirFactory.register(target.getContext(), target);
        // Retry事件
        loadSirFactory.listener(target.getContext(), reloadListener);
    }

    @BindingAdapter("loadState")
    public static void loadState(final View target, final LoadingState loadState) {
        // 非空验证
        if (loadState == null) {
            return;
        }

        LoadSirFactory loadSirFactory = LoadSirFactory.getInstance();
        // 区分状态
        switch (loadState.getLoadState()) {
            case LoadState.LOADING:
                loadSirFactory.showLoading(target.getContext(), target,
                        loadState.getLoadMessage(),
                        loadState.getLoadColor(),
                        loadState.getLoadBgColor());
                break;
            case LoadState.SUCCESS:
                loadSirFactory.showSuccess(target.getContext(), target);
                break;
            case LoadState.ERROR:
                loadSirFactory.showError(target.getContext(), target,
                        loadState.getLoadMessage(),
                        loadState.getLoadColor(),
                        loadState.getLoadBgColor(),
                        loadState.getLoadErrorIcon());
                break;
            default:
                break;
        }
    }
}
