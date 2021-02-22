package com.kaisengao.mvvm.viewmodel;

import android.app.Application;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

    private MutableLiveData<ColorDrawable> mBgColor;

    private final SingleLiveEvent<Void> mBackPressed = new SingleLiveEvent<>();

    public ToolbarViewModel(@NonNull Application application) {
        super(application);
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
     * 设置背景颜色
     *
     * @param bgColor 背景颜色
     */
    protected void setBgColor(int bgColor) {
        this.getBgColor().setValue(new ColorDrawable(ContextCompat.getColor(getApplication(), bgColor)));
    }

    /**
     * 返回事件
     */
    public final void onBackPressed() {
        this.getBackPressed().call();
    }

    public final MutableLiveData<String> getTitle() {
        return this.mTitle = createLiveData(mTitle, "");
    }

    public final MutableLiveData<ColorDrawable> getBgColor() {
        return this.mBgColor = createLiveData(mBgColor);
    }

    public SingleLiveEvent<Void> getBackPressed() {
        return this.mBackPressed;
    }
}
