package com.kaisengao.retrofit.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.kaisengao.retrofit.R;
import com.kasiengao.base.util.DensityUtil;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * @ClassName: LoadingDialog
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/1 13:41
 * @Description: Loading
 */
public class LoadingDialog extends Dialog {

    private Builder mBuilder;

    private TextView mLoadingText;

    private AVLoadingIndicatorView mLoadingView;

    private LoadingDialog(Builder builder) {
        super(builder.mContext, R.style.loading_dialog);

        this.mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.layout_loading);

        setCancelable(this.mBuilder.mCancelable);

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = DensityUtil.getDp(getContext(), 130f);
            lp.height = DensityUtil.getDp(getContext(), 100f);
            window.setAttributes(lp);
        }
        // RootView
        findViewById(R.id.loading_root).setBackgroundResource(R.drawable.shape_loading_bg);

        this.mLoadingView = findViewById(R.id.loading);
        this.mLoadingText = findViewById(R.id.loading_text);

        this.mLoadingView.setIndicatorColor(ContextCompat.getColor(getContext(), this.mBuilder.mLoadColor));

        this.mLoadingText.setTextColor(ContextCompat.getColor(getContext(), this.mBuilder.mLoadColor));
        this.mLoadingText.setText(this.mBuilder.mLoadText);
    }

    public void loadText(@StringRes int loadText) {
        this.mLoadingText.setText(loadText);
    }

    public void loadColor(@ColorRes int loadColor) {
        this.mLoadingView.setIndicatorColor(ContextCompat.getColor(getContext(), this.mBuilder.mLoadColor));
        this.mLoadingText.setTextColor(ContextCompat.getColor(getContext(), this.mBuilder.mLoadColor));
    }

    public static class Builder {

        private Context mContext;

        private int mLoadColor;

        @StringRes
        private int mLoadText;

        private boolean mCancelable = false;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder loadColor(@ColorRes int loadColor) {
            this.mLoadColor = loadColor;
            return this;
        }

        public Builder loadText(@StringRes int loadText) {
            this.mLoadText = loadText;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public LoadingDialog build() {
            return new LoadingDialog(this);
        }

        public LoadingDialog show() {
            LoadingDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }
}
