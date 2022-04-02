package com.kasiengao.ksgframe.ui.trainee.loadpage;

import android.app.Application;

import androidx.annotation.NonNull;

import com.kaisengao.mvvm.viewmodel.ToolbarViewModel;
import com.kasiengao.ksgframe.R;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: LoadPageViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/2 22:08
 * @Description: LoadPage
 */
public class LoadPageViewModel extends ToolbarViewModel {

    public LoadPageViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    /**
     * Init Toolbar
     */
    @Override
    protected void initToolbar() {
        this.setToolbarTitle(R.string.trainee_loadpage);
    }
}