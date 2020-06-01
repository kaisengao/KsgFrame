package com.kaisengao.retrofit.observer;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.kaisengao.retrofit.R;
import com.kaisengao.retrofit.exception.ExceptionHandle;
import com.kaisengao.retrofit.util.NetworkUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseRxObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:11
 * @Description: Base Observer
 */
public abstract class BaseRxObserver<T> implements Observer<T> {

    Context mContext;

    @ColorRes
    protected int mBackgroundColor = R.color.white;

    @ColorRes
    protected int mLoadingColor = R.color.black;

    @StringRes
    protected int mLoadingText = R.string.loading;

    @DrawableRes
    protected int mErrorIcon = R.drawable.icon_error;

    protected BaseRxObserver(Context context) {
        this.mContext = context;
    }

    /**
     * 设置 加载背景
     *
     * @param backgroundColor 背景 图片or颜色
     */
    public BaseRxObserver<T> setBackgroundColor(@ColorRes int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        return this;
    }

    /**
     * 设置 加载字体颜色
     *
     * @param loadingColor 颜色 R.color....
     */
    public BaseRxObserver<T> setLoadingColor(@ColorRes int loadingColor) {
        this.mLoadingColor = loadingColor;
        return this;
    }

    /**
     * 设置 加载提示语句
     *
     * @param loadingText 提示语句
     */
    public BaseRxObserver<T> setLoadingText(@StringRes int loadingText) {
        this.mLoadingText = loadingText;
        return this;
    }

    /**
     * 设置 错误图片
     *
     * @param errorIcon 错误icon
     */
    public BaseRxObserver<T> setErrorIcon(@DrawableRes int errorIcon) {
        this.mErrorIcon = errorIcon;
        return this;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T result) {

        this.onResult(result);
    }

    @Override
    public void onError(Throwable e) {

        String exception;

        if (!NetworkUtil.isNetConnected(mContext)) {
            exception = mContext.getString(R.string.net_not);
        } else {
            exception = ExceptionHandle.handleException(mContext, e);
        }

        this.onError(exception);
    }

    @Override
    public void onComplete() {

    }

    /**
     * Result
     *
     * @param t Result
     */
    protected abstract void onResult(T t);

    /**
     * Error
     *
     * @param message 错误信息
     */
    protected void onError(String message) {

    }
}
