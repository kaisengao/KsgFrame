package com.kaisengao.base.factory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.kaisengao.base.listener.OnLoadReloadListener;
import com.kaisengao.base.loadpage.KsgLoadFrame;
import com.kaisengao.base.loadpage.load.ErrorViewLoad;
import com.kaisengao.base.loadpage.load.LoadingViewLoad;
import com.kaisengao.base.loadpage.load.base.ILoad;
import com.kaisengao.base.loadpage.widget.LoadContainer;
import com.kasiengao.base.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: LoadPageFactory
 * @Author: KaiSenGao
 * @CreateDate: 2019-10-12 14:35
 * @Description: LoadPage 工厂类
 */
public class LoadPageFactory {

    private static LoadPageFactory sInstance;

    private final Map<Context, Map<Object, LoadContainer>> mLoadContainers;

    private LoadPageFactory() {
        this.mLoadContainers = new HashMap<>();
    }

    public static LoadPageFactory getInstance() {
        if (sInstance == null) {
            synchronized (LoadPageFactory.class) {
                if (sInstance == null) {
                    sInstance = new LoadPageFactory();
                }
            }
        }
        return sInstance;
    }

    /**
     * 注册异常布局
     */
    public synchronized void register(final Context context,
                                      final Object target,
                                      final OnLoadReloadListener onReloadListener) {
        Map<Object, LoadContainer> loadContainers = mLoadContainers.get(context);
        if (loadContainers == null) {
            loadContainers = new HashMap<>();
            this.mLoadContainers.put(context, loadContainers);
        }
        if (!loadContainers.containsKey(target)) {
            loadContainers.put(target, createLoadFrame(target, onReloadListener));
        }
    }

    private synchronized LoadContainer createLoadFrame(final Object target,
                                                       final OnLoadReloadListener onReloadListener) {
        return KsgLoadFrame.bindLoadContainer(target, (loadTarget, load) -> {
            if (onReloadListener != null && load instanceof ErrorViewLoad) {
                onReloadListener.onLoadReload(loadTarget);
            }
        });
    }

    public synchronized void showSuccess(final Context context,
                                         final Object target) {
        Map<Object, LoadContainer> loadContainers = mLoadContainers.get(context);
        if (loadContainers != null) {
            LoadContainer loadContainer = loadContainers.get(target);
            if (loadContainer != null) {
                loadContainer.showOriginalView();
            }
        }
    }

    public synchronized void showLoading(final Context context,
                                         final Object target,
                                         final @ColorRes int backgroundColor,
                                         final @ColorRes int color,
                                         final String message) {
        this.showLoading(context, target, backgroundColor, color, message, LoadingViewLoad.class);
    }

    public synchronized void showLoading(final Context context,
                                         final Object target,
                                         final @ColorRes int backgroundColor,
                                         final @ColorRes int color,
                                         final String message,
                                         final Class<? extends ILoad> loadView) {
        Map<Object, LoadContainer> loadContainers = mLoadContainers.get(context);
        if (loadContainers != null) {
            LoadContainer loadContainer = loadContainers.get(target);
            if (loadContainer != null) {
                loadContainer.showLoadView(loadView);
                View rootView = loadContainer.getCurLoadView().getRootView();
                // 背景色
                ViewGroup loadingRoot = rootView.findViewById(R.id.loading_root);
                if (loadingRoot != null && backgroundColor != -1) {
                    loadingRoot.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
                }
                // 加载颜色
                int newColor = ContextCompat.getColor(context, color);
                // Loading
                AVLoadingIndicatorView loadingView = rootView.findViewById(R.id.loading);
                if (loadingView != null) {
                    loadingView.setIndicatorColor(newColor);
                }
                // Loading提示
                TextView loadingText = rootView.findViewById(R.id.loading_text);
                if (loadingText != null) {
                    loadingText.setText(message);
                    loadingText.setTextColor(newColor);
                }
            }
        }
    }

    public synchronized void showError(final Context context,
                                       final Object target,
                                       final @DrawableRes int icon,
                                       final @ColorRes int backgroundColor,
                                       final @ColorRes int loadColor,
                                       final CharSequence message) {
        this.showError(context, target, icon, backgroundColor, loadColor, message, ErrorViewLoad.class);
    }

    public synchronized void showError(final Context context,
                                       final Object target,
                                       final @DrawableRes int icon,
                                       final @ColorRes int backgroundColor,
                                       final @ColorRes int loadColor,
                                       final CharSequence message,
                                       final Class<? extends ILoad> loadView) {
        Map<Object, LoadContainer> loadContainers = mLoadContainers.get(context);
        if (loadContainers != null) {
            LoadContainer loadContainer = loadContainers.get(target);
            if (loadContainer != null) {
                loadContainer.showLoadView(loadView);
                View rootView = loadContainer.getCurLoadView().getRootView();
                // 背景色
                ViewGroup errorRoot = rootView.findViewById(R.id.error_root);
                if (errorRoot != null && backgroundColor != -1) {
                    errorRoot.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
                }
                // 失败图标
                ImageView errorIcon = rootView.findViewById(R.id.error_icon);
                if (errorIcon != null) {
                    if (icon != 0) {
                        errorIcon.setImageResource(icon);
                        errorIcon.setVisibility(View.VISIBLE);
                    } else {
                        errorIcon.setVisibility(View.GONE);
                    }
                }
                // 失败提示
                TextView errorText = rootView.findViewById(R.id.error_text);
                if (errorText != null) {
                    errorText.setText(message);
                    errorText.setTextColor(ContextCompat.getColor(context, loadColor));
                }
            }
        }
    }

    /**
     * 销毁视图后初始化LoadPage
     */
    public synchronized void onDetachView(final Context context) {
        Map<Object, LoadContainer> loadContainers = mLoadContainers.get(context);
        if (loadContainers != null) {
            loadContainers.clear();
        }
        this.mLoadContainers.remove(context);
    }
}
