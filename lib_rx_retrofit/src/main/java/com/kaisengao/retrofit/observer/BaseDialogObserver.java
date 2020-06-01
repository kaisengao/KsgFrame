package com.kaisengao.retrofit.observer;

import android.app.ProgressDialog;
import android.content.Context;

import com.kaisengao.retrofit.widget.LoadingDialog;
import com.kasiengao.base.util.ToastUtil;

import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseDialogObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:28
 * @Description: 绑定Dialog的Observer
 */
public abstract class BaseDialogObserver<T> extends BaseRxObserver<T> {

    private LoadingDialog mLoadingDialog;

    protected BaseDialogObserver(Context context) {
        super(context);

        this.initDialog();
    }

    /**
     * 初始化Loading
     */
    private void initDialog() {
        this.mLoadingDialog = new LoadingDialog.Builder(mContext)
                .loadText(mLoadingText)
                .loadColor(mLoadingColor)
                .build();
    }

    /**
     * 设置 加载提示语句
     *
     * @param loadingText 提示语句
     */
    @Override
    public BaseRxObserver<T> setLoadingText(int loadingText) {
        this.mLoadingDialog.loadText(loadingText);
        return super.setLoadingText(loadingText);
    }

    /**
     * 设置 加载字体颜色
     *
     * @param loadingColor 颜色 R.color....
     */
    @Override
    public BaseRxObserver<T> setLoadingColor(int loadingColor) {
        this.mLoadingDialog.loadColor(loadingColor);
        return super.setLoadingColor(loadingColor);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);

        this.mLoadingDialog.show();
    }

    @Override
    protected void onError(String message) {

        ToastUtil.showShortSafe(message);
    }

    @Override
    public void onComplete() {
        this.mLoadingDialog.dismiss();
    }

}
