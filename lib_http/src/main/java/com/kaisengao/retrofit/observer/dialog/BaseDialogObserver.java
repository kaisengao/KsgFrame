package com.kaisengao.retrofit.observer.dialog;

import com.kaisengao.base.util.TextUtil;
import com.kaisengao.base.util.ToastUtil;
import com.kaisengao.retrofit.observer.BaseRxObserver;
import com.kaisengao.retrofit.widget.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseDialogObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:28
 * @Description: 绑定Dialog的Observer
 */
public abstract class BaseDialogObserver<T> extends BaseRxObserver<T> {

    private LoadingDialog mLoadingDialog;

    protected BaseDialogObserver() {
    }

    /**
     * 设置 提示语句
     *
     * @param loadMessage 提示语句
     */
    @Override
    public BaseRxObserver<T> setLoadMessage(int loadMessage) {
        super.setLoadMessage(loadMessage);
        if (mLoadingDialog != null) {
            this.mLoadingDialog.loadMessage(TextUtil.getString(mLoadMessage));
        }
        return this;
    }

    /**
     * 设置 提示颜色
     *
     * @param loadColor 颜色 R.color....
     */
    @Override
    public BaseRxObserver<T> setLoadColor(int loadColor) {
        super.setLoadColor(loadColor);
        if (mLoadingDialog != null) {
            this.mLoadingDialog.loadColor(mLoadColor);
        }
        return this;
    }

    @Override
    public void onSubscribe(@NotNull Disposable d) {
        super.onSubscribe(d);
        // show
        this.initDialog();
        this.mLoadingDialog.show();
    }

    @Override
    protected void onError(String message) {
        // 错误 提示
        ToastUtil.showShortSafe(message);
        // 完成
        this.onComplete();
    }

    @Override
    public void onComplete() {
        if (mLoadingDialog != null) {
            this.mLoadingDialog.dismiss();
        }
    }

    /*
     * 初始化 LoadingDialog
     */
    private void initDialog() {
        if (this.mLoadingDialog == null) {
            this.mLoadingDialog = new LoadingDialog.Builder()
                    .setLoadMessage(TextUtil.getString(mLoadMessage))
                    .setLoadColor(mLoadColor)
                    .build();
        }
    }
}
