package com.kasiengao.ksgframe.ui.trainee.gesture;

import android.app.Application;

import androidx.annotation.NonNull;

import com.kaisengao.mvvm.viewmodel.ToolbarViewModel;
import com.kasiengao.ksgframe.R;

/**
 * @ClassName: GestureViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/2 22:05
 * @Description: Gesture
 */
public class GestureViewModel extends ToolbarViewModel {

    public GestureViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Init Toolbar
     */
    @Override
    protected void initToolbar() {
        this.setToolbarTitle(R.string.trainee_gesture);
    }
}