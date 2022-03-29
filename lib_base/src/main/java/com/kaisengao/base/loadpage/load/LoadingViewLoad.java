package com.kaisengao.base.loadpage.load;

import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.base.loadpage.load.base.BaseLoad;
import com.kasiengao.base.R;

/**
 * @ClassName: LoadingLoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 11:50
 * @Description: Loading视图
 */
public class LoadingViewLoad extends BaseLoad {

    private AppCompatTextView mLoadingTextView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_loading;
    }

    /**
     * View与页面绑定
     */
    @Override
    protected void onViewBind() {
        this.mLoadingTextView = findViewById(R.id.loading_text);
    }

    /**
     * 设置 提示文字
     *
     * @param loadingText Loading提示文字
     */
    public void setLoadingText(CharSequence loadingText) {
        this.mLoadingTextView.setText(loadingText);
    }

    @Override
    protected boolean onClickEvent() {
        return true;
    }
}