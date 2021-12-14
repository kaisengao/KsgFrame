package com.kasiengao.ksgframe.ui.trainee.grid;

import android.app.Application;

import androidx.annotation.NonNull;

import com.kaisengao.mvvm.viewmodel.ToolbarViewModel;

/**
 * @ClassName: TouchGridViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2021/6/9 10:48
 * @Description:
 */
public class TouchGridViewModel extends ToolbarViewModel {

    public TouchGridViewModel(@NonNull Application application) {
        super(application);
        // Title
        this.getTitle().setValue("拖拽Grid");
    }
}