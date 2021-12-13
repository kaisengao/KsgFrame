package com.kaisengao.retrofit;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.kaisengao.retrofit.factory.GsonBuilderFactory;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName: RxCompose
 * @Author: KaiSenGao
 * @CreateDate: 2019-09-29 14:58
 * @Description:
 */
public class RxCompose {

    /**
     * Json Obj类型解析
     *
     * @param <T>    泛型
     * @param tClass 类型
     * @return T
     */
    public static <T> ObservableTransformer<String, T> fromJsonObj(Class<T> tClass) {
        return new ObservableTransformer<String, T>() {
            @Override
            public ObservableSource<T> apply(Observable<String> upstream) {
                return upstream.flatMap(new Function<String, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(String json) throws Exception {
                        // 返回result
                        return Observable.just(GsonBuilderFactory.getInstance().formJson(json, tClass));
                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<BaseResult<T>, T> handleResult() {
        return new ObservableTransformer<BaseResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResult<T>> upstream) {
                return upstream.flatMap(new Function<BaseResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(BaseResult<T> baseResult) throws Exception {
                        // 验证请求码是否正确
                        if (baseResult.isSuccess()) {
                            return Observable.just(baseResult.getResult());
                        }
                        // 请求码不对 或 请求失败错误
                        return Observable.error(new Exception(baseResult.getMessage()));
                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 线程调度
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 绑定生命周期
     */
    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner owner) {
        return bindLifecycle(owner, Lifecycle.Event.ON_DESTROY);
    }

    /**
     * 绑定生命周期
     */
    public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner owner, Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, untilEvent));
    }
}
