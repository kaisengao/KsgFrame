package com.kaisengao.base.state;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.kaisengao.base.loadpage.load.base.ILoad;

/**
 * @ClassName: LoadingState
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/19 15:30
 * @Description:
 */
public class LoadingState {

    @LoadState
    private int mLoadState;
    @ColorRes
    private int mLoadColor;
    @ColorRes
    private int mLoadBgColor;
    @DrawableRes
    private int mLoadErrorIcon;

    private String mLoadMessage;

    private Class<? extends ILoad> mLoadingView;

    public int getLoadState() {
        return mLoadState;
    }

    public void setLoadState(int loadState) {
        mLoadState = loadState;
    }

    public int getLoadColor() {
        return mLoadColor;
    }

    public void setLoadColor(int loadColor) {
        mLoadColor = loadColor;
    }

    public int getLoadBgColor() {
        return mLoadBgColor;
    }

    public void setLoadBgColor(int loadBgColor) {
        mLoadBgColor = loadBgColor;
    }

    public int getLoadErrorIcon() {
        return mLoadErrorIcon;
    }

    public void setLoadErrorIcon(int loadErrorIcon) {
        mLoadErrorIcon = loadErrorIcon;
    }

    public String getLoadMessage() {
        return mLoadMessage;
    }

    public void setLoadMessage(String loadMessage) {
        mLoadMessage = loadMessage;
    }
    public void setLoadingView(Class<? extends ILoad> loadingView) {
        mLoadingView = loadingView;
    }

    public Class<? extends ILoad> getLoadingView() {
        return mLoadingView;
    }
}
