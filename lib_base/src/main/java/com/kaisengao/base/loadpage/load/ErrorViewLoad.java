package com.kaisengao.base.loadpage.load;

import androidx.appcompat.widget.AppCompatTextView;

import com.kasiengao.base.R;

/**
 * @ClassName: ErrorViewLoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 13:27
 * @Description: Error视图
 */
public class ErrorViewLoad extends BaseLoad {

    private AppCompatTextView mErrorTextView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_error;
    }

    /**
     * View与页面绑定
     */
    @Override
    protected void onModeViewBind() {
        this.mErrorTextView = findViewById(R.id.error_text);
        this.findViewById(R.id.error_retry).setOnClickListener(v -> onLoadClick());
    }

    /**
     * 设置 提示文字
     *
     * @param errorText Error提示文字
     */
    public void setErrorText(CharSequence errorText) {
        this.mErrorTextView.setText(errorText);
    }

    @Override
    protected boolean onClickEvent() {
        return true;
    }
}