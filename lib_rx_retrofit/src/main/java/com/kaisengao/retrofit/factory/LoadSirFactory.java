package com.kaisengao.retrofit.factory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.kaisengao.retrofit.R;
import com.kaisengao.retrofit.listener.OnLoadSirReloadListener;
import com.kasiengao.base.loadsir.callback.Callback;
import com.kasiengao.base.loadsir.callback.ErrorCallback;
import com.kasiengao.base.loadsir.callback.LoadingCallback;
import com.kasiengao.base.loadsir.core.LoadService;
import com.kasiengao.base.loadsir.core.LoadSir;
import com.kasiengao.base.loadsir.core.Transport;

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
                                         final @ColorRes int color,
                                         final @ColorRes int backgroundColor,
                                         final String message) {

        ConcurrentHashMap<Object, LoadService> loadServices = mContextLoadServices.get(context);

        if (loadServices != null) {

            LoadService loadService = loadServices.get(target);

            if (loadService != null) {

                loadService.showCallback(LoadingCallback.class);
                loadService.setCallBack(LoadingCallback.class, new Transport() {
                    @Override
                    public void order(Context context, View view) {
                        if (backgroundColor != -1) {
                            view.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
                        }

                        TextView loadingText = view.findViewById(R.id.loading_text);

                        loadingText.setText(message);
                        loadingText.setTextColor(ContextCompat.getColor(context, color));
                    }
                });

            }
        }
    }

    public synchronized void showError(final Context context,
                                       final Object target,
                                       final @DrawableRes int icon,
                                       final @ColorRes int backgroundColor,
                                       final @ColorRes int color,
                                       final String message) {

        ConcurrentHashMap<Object, LoadService> loadServices = mContextLoadServices.get(context);

        if (loadServices != null) {

            LoadService loadService = loadServices.get(target);

            if (loadService != null) {

                loadService.showCallback(ErrorCallback.class);
                loadService.setCallBack(ErrorCallback.class, new Transport() {
                    @Override
                    public void order(Context context, View view) {
                        if (backgroundColor != -1) {
                            ViewGroup errorRoot = view.findViewById(R.id.error_root);
                            errorRoot.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
                        }

                        ImageView errorImg = view.findViewById(R.id.error_icon);
                        TextView errorMsg = view.findViewById(R.id.error_msg);

                        if (icon == 0) {
                            errorImg.setVisibility(View.GONE);
                        } else {
                            errorImg.setVisibility(View.VISIBLE);
                            errorImg.setImageResource(icon);
                        }

                        errorMsg.setTextColor(ContextCompat.getColor(context, color));
                        errorMsg.setText(message);
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
