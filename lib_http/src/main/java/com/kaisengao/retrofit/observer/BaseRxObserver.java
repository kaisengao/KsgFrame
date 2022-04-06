package com.kaisengao.retrofit.observer;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.kaisengao.retrofit.R;
import com.kaisengao.retrofit.exception.ExceptionHandle;
import com.kaisengao.retrofit.util.NetworkUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseRxObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:11
 * @Description: Base Observer
 */
public abstract class BaseRxObserver<T> implements Observer<T> {

    protected Context mContext;

    @StringRes
    protected int mLoadMessage;
    @ColorRes
    protected int mLoadColor;
    @ColorRes
    protected int mLoadBgColor;
    @DrawableRes
    protected int mLoadErrorIcon = 0;

    protected BaseRxObserver(Context context) {
        this.mContext = context;
        this.setLoadMessage(R.string.loading);
        this.setLoadColor(R.color.textColorPrimary);
        this.setLoadBgColor(R.color.translucent);
    }

    /**
     * 设置 提示语句
     *
     * @param loadMessage 提示语句
     */
    public BaseRxObserver<T> setLoadMessage(@StringRes int loadMessage) {
        this.mLoadMessage = loadMessage;
        return this;
    }

    /**
     * 设置 提示颜色
     *
     * @param loadColor 颜色 R.color....
     */
    public BaseRxObserver<T> setLoadColor(@ColorRes int loadColor) {
        this.mLoadColor = loadColor;
        return this;
    }

    /**
     * 设置 提示背景颜色
     *
     * @param loadBgColor 颜色 R.color....
     */
    public BaseRxObserver<T> setLoadBgColor(@ColorRes int loadBgColor) {
        this.mLoadBgColor = loadBgColor;
        return this;
    }

    /**
     * 设置 提示错误图标
     *
     * @param loadErrorIcon 图标 R.mipmap....
     */
    public BaseRxObserver<T> setLoadErrorIcon(int loadErrorIcon) {
        this.mLoadErrorIcon = loadErrorIcon;
        return this;
    }

    @Override
    public void onSubscribe(@NotNull Disposable d) {

    }

    /**
     * onNext
     *
     * @param result <T>
     */
    @Override
    public void onNext(@NotNull T result) {
        this.onResult(result);
    }

    /**
     * Error 事件
     *
     * @param throwable Throwable
     */
    @Override
    public void onError(@NotNull Throwable throwable) {
        String exception;
        if (!NetworkUtil.isNetConnected(mContext)) {
            exception = mContext.getString(R.string.net_not);
        } else {
            exception = ExceptionHandle.handleException(mContext, throwable);
        }
        this.onError(throwable, exception);
    }

    @Override
    public void onComplete() {

    }

    /**
     * Result
     *
     * @param t Result
     */
    protected abstract void onResult(@NotNull T t);

    /**
     * Error
     *
     * @param throwable throwable
     * @param message   错误信息
     */
    protected void onError(@NonNull Throwable throwable, String message) {
        this.onError(message);
    }

    /**
     * Error
     *
     * @param message 错误信息
     */
    protected void onError(String message) {

    }
}
