package com.kaisengao.retrofit.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.kaisengao.retrofit.R;
import com.kaisengao.base.configure.ActivityManager;
import com.kaisengao.base.util.DensityUtil;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * @ClassName: LoadingDialog
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/1 13:41
 * @Description: Loading
 */
public class LoadingDialog extends Dialog {

    private AVLoadingIndicatorView mLoadView;

    private AppCompatTextView mLoadText;

    private final Builder mBuilder;

    private LoadingDialog(Builder builder) {
        super(builder.mContext, R.style.loading_dialog);
        this.mBuilder = builder;
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.layout_loading);
        this.setCancelable(mBuilder.mCancelable);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = DensityUtil.getDp(getContext(), 130f);
            lp.height = DensityUtil.getDp(getContext(), 100f);
            window.setAttributes(lp);
        }

        LinearLayout loadRoot = findViewById(R.id.loading_root);
        this.mLoadView = findViewById(R.id.loading);
        this.mLoadText = findViewById(R.id.loading_text);

        // 初始化
        loadRoot.setBackgroundResource(R.drawable.shape_loading_bg);
        this.loadMessage(mBuilder.mLoadMessage);
        this.loadColor(mBuilder.mLoadColor);
    }

    /**
     * 提示语句
     *
     * @param loadMessage loadMessage
     */
    public void loadMessage(String loadMessage) {
        if (mLoadText != null) {
            this.mLoadText.setText(loadMessage);
        }
    }

    /**
     * 提示颜色
     *
     * @param loadColor loadColor
     */
    public void loadColor(@ColorRes int loadColor) {
        if (this.mLoadText != null) {
            this.mLoadText.setTextColor(ContextCompat.getColor(getContext(), loadColor));
            this.mLoadView.setIndicatorColor(ContextCompat.getColor(getContext(), loadColor));
        }
    }

    public static class Builder {

        private Context mContext;

        private String mLoadMessage;
        @ColorRes
        private int mLoadColor;

        private boolean mCancelable = false;

        public Builder() {
            this.mContext = ActivityManager.getInstance().currentActivity();
        }

        public Builder setLoadMessage(String loadMessage) {
            this.mLoadMessage = loadMessage;
            return this;
        }

        public Builder setLoadColor(@ColorRes int loadColor) {
            this.mLoadColor = loadColor;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public LoadingDialog build() {
            return new LoadingDialog(this);
        }
    }
}
