package com.kaisengao.mvvm.binding.adapter.loadpage;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.kaisengao.base.annotations.ReloadAnnotations;
import com.kaisengao.base.factory.LoadPageFactory;
import com.kaisengao.base.state.LoadState;
import com.kaisengao.base.state.LoadingState;
import com.kaisengao.mvvm.binding.command.BindingParamImp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName: ViewAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/18 14:36
 * @Description: 绑定 LoadPage
 */
public class LoadPageAdapter {

    @BindingAdapter("loadRegister")
    public static void loadRegister(final View register,
                                    @Nullable final BindingParamImp<Object> onReloadImp) {
        // 存储TAG
        LoadPageFactory loadSirFactory = LoadPageFactory.getInstance();
        // 注册LoadPage
        loadSirFactory.register(register.getContext(), register, target -> {
            // Retry事件
            if (onReloadImp != null) {
                onReloadImp.execute(target);
            } else {
                // 反射回调方法
                injectReload(register.getContext(), target);
            }
        });
    }

    @BindingAdapter("loadState")
    public static void loadState(final View target, final LoadingState loadState) {
        // 非空验证
        if (loadState == null) {
            return;
        }
        LoadPageFactory loadSirFactory = LoadPageFactory.getInstance();
        // 区分状态
        switch (loadState.getLoadState()) {
            case LoadState.LOADING:
                loadSirFactory.showLoading(target.getContext(), target,
                        loadState.getLoadBgColor(),
                        loadState.getLoadColor(),
                        loadState.getLoadMessage(),
                        loadState.getLoadingView());
                break;
            case LoadState.SUCCESS:
                loadSirFactory.showSuccess(target.getContext(), target);
                break;
            case LoadState.ERROR:
                loadSirFactory.showError(target.getContext(), target,
                        loadState.getLoadErrorIcon(),
                        loadState.getLoadBgColor(),
                        loadState.getLoadColor(),
                        loadState.getLoadMessage());
                break;
            default:
                break;
        }
    }

    /**
     * 根据反射找到注解方法
     *
     * @param object class
     * @param args   携带参数
     */
    private static void injectReload(final Object object, final Object... args) {
        // 非空验证
        if (object == null) {
            return;
        }
        // 反射查找方法 进行回调
        Class<?> objClass = object.getClass();
        // 获取该Class下的所有方法
        Method[] methods = objClass.getDeclaredMethods();
        // 遍历方法，找到注解方法
        for (Method method : methods) {
            ReloadAnnotations annotation = method.getAnnotation(ReloadAnnotations.class);
            // 非空验证
            if (annotation != null) {
                try {
                    // 注入回调
                    method.invoke(object, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
