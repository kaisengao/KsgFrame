package com.kaisengao.retrofit.observer;

import android.app.ProgressDialog;
import android.content.Context;

import com.kasiengao.base.util.ToastUtil;

import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseDialogObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:28
 * @Description: 绑定Dialog的Observer
 */
public abstract class BaseDialogObserver<T> extends BaseRxObserver<T> {

    private ProgressDialog mDialog;

    protected BaseDialogObserver(Context context) {
        super(context);
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        if (this.mContext == null) {
            throw new IllegalArgumentException("You haven't set the context.");
        }

        if (this.mDialog == null) {
            new ProgressDialog
                    .Builder(this.mContext)
                    .create()
//                    .loadText(this.mLoadingText)
//                    .loadColor(this.mLoadingColor)
                    .show();
        } else {
            this.mDialog.show();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);

        this.initDialog();
    }

    @Override
    protected void onError(int icon, int backgroundColor, int color, String message) {
        super.onError(icon, backgroundColor, color, message);

        ToastUtil.showShortSafe(message);

        this.onComplete();
    }

    @Override
    public void onComplete() {
        super.onComplete();

        if (this.mDialog != null) {
            this.mDialog.dismiss();
        }
    }
}
