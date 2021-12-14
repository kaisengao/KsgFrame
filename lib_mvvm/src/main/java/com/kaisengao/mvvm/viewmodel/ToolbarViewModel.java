package com.kaisengao.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.mvvm.base.viewmodel.BaseViewModel;
import com.kaisengao.mvvm.event.SingleLiveEvent;

/**
 * @ClassName: ToolbarViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/30 15:09
 * @Description: ToolbarViewModel
 */
public class ToolbarViewModel extends BaseViewModel {

    private MutableLiveData<String> mTitle;

    private MutableLiveData<Integer> mNavigationIcon;

    private MutableLiveData<Integer> mMenuRes;

    private SingleLiveEvent<Void> mBackPressed;

    private SingleLiveEvent<Void> mMenuClick;

    public ToolbarViewModel(@NonNull Application application) {
        super(application);
        // Init Toolbar
        this.initToolbar();
    }

    /**
     * Init Toolbar
     */
    protected void initToolbar() {

    }

    /**
     * 设置标题
     *
     * @param title int 资源文件
     */
    protected void setToolbarTitle(int title) {
        this.setToolbarTitle(getApplication().getString(title));
    }

    /**
     * 设置标题
     *
     * @param title title
     */
    protected void setToolbarTitle(String title) {
        this.getTitle().setValue(title);
    }

    /**
     * 设置导航资源
     *
     * @param resId 资源
     */
    protected void setNavigationIcon(int resId) {
        this.getNavigationIcon().setValue(resId);
    }

    /**
     * 设置菜单资源
     *
     * @param resId 资源
     */
    protected void setMenuRes(@DrawableRes int resId) {
        this.getMenuRes().setValue(resId);
    }


    /**
     * 返回事件
     */
    public void onBackPressed() {
        this.getBackPressed().call();
    }

    /**
     * 功能键事件
     */
    public void onMenuClick() {
        this.getMenuClick().call();
    }

    public final MutableLiveData<String> getTitle() {
        return mTitle = createLiveData(mTitle, "PDA");
    }

    public final MutableLiveData<Integer> getNavigationIcon() {
        return mNavigationIcon = createLiveData(mNavigationIcon, 0);
    }

    public final MutableLiveData<Integer> getMenuRes() {
        return mMenuRes = createLiveData(mMenuRes, 0);
    }

    public final SingleLiveEvent<Void> getBackPressed() {
        return mBackPressed = createSingleLiveData(mBackPressed);
    }

    public final SingleLiveEvent<Void> getMenuClick() {
        return mMenuClick = createSingleLiveData(mMenuClick);
    }
}
