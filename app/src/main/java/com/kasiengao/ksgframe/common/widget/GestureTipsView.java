package com.kasiengao.ksgframe.common.widget;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kasiengao.ksgframe.R;

/**
 * @ClassName: GestureTipsView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/28 15:41
 * @Description: 手势操作 提示View
 */
public class GestureTipsView {

    private View mRootView;

    private boolean mSliding = false;

    public GestureTipsView setRooView(View rooView) {
        this.mRootView = rooView;
        return this;
    }

    public GestureTipsView setBrightnessIcon(AppCompatImageView imageView, int brightness) {
        if (brightness < 20) {
            imageView.setImageResource(R.drawable.ic_cover_brightness_low);
        } else if (brightness < 70) {
            imageView.setImageResource(R.drawable.ic_cover_brightness_medium);
        } else {
            imageView.setImageResource(R.drawable.ic_cover_brightness_high);
        }
        return this;
    }

    public GestureTipsView setVolumeIcon(AppCompatImageView imageView, int volume) {
        if (volume < 5) {
            imageView.setImageResource(R.drawable.ic_cover_volume_mute);
        } else if (volume < 60) {
            imageView.setImageResource(R.drawable.ic_cover_volume_down);
        } else {
            imageView.setImageResource(R.drawable.ic_cover_volume_up);
        }
        return this;
    }

    public GestureTipsView setSliding(AppCompatImageView imageView, float progress) {
        if (progress < 0) {
            imageView.setImageResource(R.drawable.ic_cover_fast_rewind);
        } else {
            imageView.setImageResource(R.drawable.ic_cover_fast_forward);
        }
        return this;
    }

    public GestureTipsView setProgress(ProgressBar progressBar, int progress) {
        progressBar.setProgress(progress);
        return this;
    }

    public GestureTipsView setTime(AppCompatTextView textView, String time) {
        textView.setText(time);
        return this;
    }

    public void show() {
        if (!mSliding) {
            mSliding = true;
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(200);
            this.mRootView.startAnimation(alphaAnimation);
        }
    }

    public void dismiss() {
        if (mSliding) {
            mSliding = false;
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(200);
            this.mRootView.startAnimation(alphaAnimation);
        }
    }
}
