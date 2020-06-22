package com.kaisengao.retrofit.observer.dialog;

import android.content.Context;

import com.kaisengao.retrofit.observer.BaseRxObserver;
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
            this.mLoadingDialog.loadMessage(mContext.getString(mLoadMessage));
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
    public void onSubscribe(Disposable d) {
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
                    .setLoadMessage(mContext.getString(mLoadMessage))
                    .setLoadColor(mLoadColor)
                    .build();
        }
    }
}
