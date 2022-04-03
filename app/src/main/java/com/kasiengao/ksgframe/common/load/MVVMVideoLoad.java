package com.kasiengao.ksgframe.common.load;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.kaisengao.base.loadpage.load.base.BaseLoad;
import com.kaisengao.base.util.DensityUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.factory.AppFactory;

/**
 * @ClassName: MVVMVideoLoad
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 21:22
 * @Description: Load
 */
public class MVVMVideoLoad extends BaseLoad {

    private final int[] mScreenSize;

    private ObjectAnimator mAnimY;

    public MVVMVideoLoad() {
        this.mScreenSize = DensityUtil.getScreenSize(AppFactory.application());
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_mvvm_video_load;
    }

    @Override
    protected void onViewBind() {
        if (mAnimY == null) {
            this.mAnimY = ObjectAnimator.ofFloat(findViewById(R.id.load), View.X, -250, mScreenSize[0] + 250);
            this.mAnimY.setInterpolator(new LinearInterpolator());
            this.mAnimY.setRepeatCount(-1);
            this.mAnimY.setDuration(1000);
        }
        this.mAnimY.start();
    }

    @Override
    protected void onViewUnBind() {
        if (mAnimY != null) {
            this.mAnimY.cancel();
            this.mAnimY = null;
        }
    }

    @Override
    protected boolean onClickEvent() {
        return true;
    }
}