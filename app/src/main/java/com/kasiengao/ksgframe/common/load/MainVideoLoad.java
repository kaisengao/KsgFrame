package com.kasiengao.ksgframe.common.load;

import android.view.View;
import android.view.animation.AnimationUtils;

import com.kaisengao.base.loadpage.load.base.BaseLoad;
import com.kasiengao.ksgframe.R;

/**
 * @ClassName: MainVideoLoad
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 21:22
 * @Description: 首页视频Load
 */
public class MainVideoLoad extends BaseLoad {

    private View mLightView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_main_video_loading;
    }

    @Override
    protected void onViewBind() {
        this.mLightView = findViewById(R.id.light);
        this.mLightView.startAnimation(AnimationUtils.loadAnimation(mLightView.getContext(), R.anim.anim_load_light));
    }

    @Override
    protected void onViewUnBind() {
        if (mLightView != null) {
            this.mLightView.clearAnimation();
        }
    }

    @Override
    protected boolean onClickEvent() {
        return true;
    }
}