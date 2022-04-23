package com.kaisengao.retrofit.compose;

import com.kaisengao.retrofit.BaseResult;
import com.kaisengao.retrofit.factory.GsonBuilderFactory;

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

    public static <T> ObservableTransformer<String, T> handleJsonObj(Class<T> tClass) {
        return upstream -> upstream.flatMap((Function<String, ObservableSource<T>>) json -> {
            // Return
            return Observable.just(GsonBuilderFactory.getInstance().fromJson(json, tClass));
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<BaseResult<T>, T> handleResult() {
        return upstream -> upstream.flatMap((Function<BaseResult<T>, ObservableSource<T>>) result -> {
            // Code
            if (result.isSuccess()) {
                // Success
                return Observable.just(result.getResult());
            }
            // Fail
            return Observable.error(new Exception(result.getMessage()));
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 线程调度
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
