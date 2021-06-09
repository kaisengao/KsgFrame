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

    private MutableLiveData<Boolean> mBackVisibility;

    private MutableLiveData<Integer> mBackDrawable;

    private MutableLiveData<Boolean> mMenuVisibility;

    private MutableLiveData<Integer> mMenuDrawable;

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
     * 设置返回键
     *
     * @param isVisibility 显示/隐藏
     */
    protected void setBackVisibility(boolean isVisibility) {
        this.getBackVisibility().setValue(isVisibility);
    }


    /**
     * 设置返回键图标
     *
     * @param resId 资源图标
     */
    protected void setBackDrawable(@DrawableRes int resId) {
        this.getBackDrawable().setValue(resId);
    }

    /**
     * 设置功能键
     *
     * @param isVisibility 显示/隐藏
     */
    protected void setMenuVisibility(boolean isVisibility) {
        this.getMenuVisibility().setValue(isVisibility);
    }

    /**
     * 设置功能键图标
     *
     * @param resId 资源图标
     */
    protected void setMenuDrawable(@DrawableRes int resId) {
        this.getMenuDrawable().setValue(resId);
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

    public final MutableLiveData<Boolean> getBackVisibility() {
        return mBackVisibility = createLiveData(mBackVisibility, true);
    }

    public final MutableLiveData<Integer> getBackDrawable() {
        return mBackDrawable = createLiveData(mBackDrawable, 0);
    }

    public final MutableLiveData<Boolean> getMenuVisibility() {
        return mMenuVisibility = createLiveData(mMenuVisibility, false);
    }

    public final MutableLiveData<Integer> getMenuDrawable() {
        return mMenuDrawable = createLiveData(mMenuDrawable, 0);
    }

    public final SingleLiveEvent<Void> getBackPressed() {
        return mBackPressed = createSingleLiveData(mBackPressed);
    }

    public final SingleLiveEvent<Void> getMenuClick() {
        return mMenuClick = createSingleLiveData(mMenuClick);
    }
}
