package com.kaisengao.retrofit.factory;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kaisengao.retrofit.R;
import com.kaisengao.retrofit.listener.OnLoadSirReloadListener;
import com.kasiengao.base.loadsir.callback.Callback;
import com.kasiengao.base.loadsir.callback.ErrorCallback;
import com.kasiengao.base.loadsir.callback.LoadingCallback;
import com.kasiengao.base.loadsir.core.LoadService;
import com.kasiengao.base.loadsir.core.LoadSir;
import com.kasiengao.base.loadsir.core.Transport;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: LoadSirFactory
 * @Author: KaiSenGao
 * @CreateDate: 2019-10-12 14:35
 * @Description: LoadSir工厂类
 */
public class LoadSirFactory {

    private static LoadSirFactory sInstance;

    private ConcurrentHashMap<Context, ConcurrentHashMap<Object, LoadService>> mContextLoadServices;

    private ConcurrentHashMap<Context, OnLoadSirReloadListener> mReloadListeners;

    private LoadSirFactory() {

        this.mContextLoadServices = new ConcurrentHashMap<>();

        this.mReloadListeners = new ConcurrentHashMap<>();
    }

    public static LoadSirFactory getInstance() {
        if (sInstance == null) {
            synchronized (LoadSirFactory.class) {
                if (sInstance == null) {
                    sInstance = new LoadSirFactory();
                }
            }
        }
        return sInstance;
    }

    public synchronized void listener(Context context, OnLoadSirReloadListener onLoadSirReloadListener) {

        mReloadListeners.put(context, onLoadSirReloadListener);
    }

    /**
     * 注册异常布局
     */
    public synchronized void register(final Context context, final Object target) {

        ConcurrentHashMap<Object, LoadService> loadServices = mContextLoadServices.get(context);

        if (loadServices == null) {

            loadServices = new ConcurrentHashMap<>();

            mContextLoadServices.put(context, loadServices);
        }

        if (!loadServices.containsKey(target)) {

            LoadService loadService = createLoadSir(target);

            loadServices.put(target, loadService);
        }
    }

    private synchronized LoadService createLoadSir(Object target) {
        return LoadSir.getDefault().register(target, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {

                OnLoadSirReloadListener onReloadListener = mReloadListeners.get(v.getContext());

                if (onReloadListener != null) {

                    onReloadListener.onLoadSirReload(target);
                }
            }
        });
    }

    public synchronized void showSuccess(final Context context, final Object target) {

        ConcurrentHashMap<Object, LoadService> loadServices = mContextLoadServices.get(context);

        if (loadServices != null) {

            LoadService loadService = loadServices.get(target);

            if (loadService != null) {

                loadService.showSuccess();
            }
        }
    }

    public synchronized void showLoading(final Context context,
                                         final Object target,
                                         final String loadMessage,
                                         final ColorDrawable loadColor,
                                         final ColorDrawable loadBgColor) {

        ConcurrentHashMap<Object, LoadService> loadServices = mContextLoadServices.get(context);

        if (loadServices != null) {

            LoadService loadService = loadServices.get(target);

            if (loadService != null) {

                loadService.showCallback(LoadingCallback.class);
                loadService.setCallBack(LoadingCallback.class, new Transport() {
                    @Override
                    public void order(Context context, View view) {
                        TextView loadingText = view.findViewById(R.id.loading_text);
                        AVLoadingIndicatorView indicatorView = view.findViewById(R.id.loading);
                        if (!TextUtils.isEmpty(loadMessage)) {
                            loadingText.setText(loadMessage);
                        }
                        if (loadColor != null) {
                            loadingText.setTextColor(loadColor.getColor());
                            indicatorView.setIndicatorColor(loadColor.getColor());
                        }
                        if (loadBgColor != null) {
                            view.setBackground(loadBgColor);
                        }
                    }
                });

            }
        }
    }

    public synchronized void showError(final Context context,
                                       final Object target,
                                       final String loadMessage,
                                       final ColorDrawable loadColor,
                                       final ColorDrawable loadBgColor) {

        ConcurrentHashMap<Object, LoadService> loadServices = mContextLoadServices.get(context);

        if (loadServices != null) {

            LoadService loadService = loadServices.get(target);

            if (loadService != null) {

                loadService.showCallback(ErrorCallback.class);
                loadService.setCallBack(ErrorCallback.class, new Transport() {
                    @Override
                    public void order(Context context, View view) {
                        TextView errorMsg = view.findViewById(R.id.error_msg);
                        if (!TextUtils.isEmpty(loadMessage)) {
                            errorMsg.setText(loadMessage);
                        }
                        if (loadColor != null) {
                            errorMsg.setTextColor(loadColor.getColor());
                        }
                        if (loadBgColor != null) {
                            view.setBackground(loadBgColor);
                        }
                    }
                });
            }
        }
    }

    /**
     * 销毁视图后初始化LoadSir
     */
    public synchronized void onDetachView(Context context) {

        ConcurrentHashMap<Object, LoadService> loadServices = mContextLoadServices.get(context);

        if (loadServices != null) {

            loadServices.clear();
        }

        mContextLoadServices.remove(context);

        mReloadListeners.remove(context);
    }

}
