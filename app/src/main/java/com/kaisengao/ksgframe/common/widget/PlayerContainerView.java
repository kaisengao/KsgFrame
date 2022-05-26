package com.kaisengao.ksgframe.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.kaisengao.base.util.GlideUtil;
import com.kaisengao.ksgframe.R;

/**
 * @ClassName: PlayerContainerView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/27 16:57
 * @Description: 播放器容器 View 最高优先级
 */
public class PlayerContainerView extends FrameLayout {

    private boolean mIntercept;

    private AppCompatImageView mPlayIcon;

    private AppCompatImageView mCoverImage;

    public PlayerContainerView(@NonNull Context context) {
        this(context, null);
    }

    public PlayerContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        View controlView = View.inflate(getContext(), R.layout.layout_play_container, this);
        this.mPlayIcon = controlView.findViewById(R.id.play_icon);
        this.mCoverImage = controlView.findViewById(R.id.cover_image);
        // HideView
        this.onHideView();
    }

    /**
     * 设置 Play图标展示
     *
     * @param showed show/hide
     */
    public void setPlayShowed(boolean showed) {
        this.mPlayIcon.setVisibility(showed ? VISIBLE : GONE);
    }

    /**
     * 设置 封面图
     *
     * @param imageUrl imageUrl
     */
    public void setCoverImage(String imageUrl, ImageView.ScaleType scaleType) {
        if (!TextUtils.isEmpty(imageUrl)) {
            GlideUtil.loadImage(getContext(), imageUrl, mCoverImage);
            this.mCoverImage.setScaleType(scaleType);
            this.mCoverImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示View
     */
    public void onShowView() {
        this.mPlayIcon.setVisibility(VISIBLE);
        this.mCoverImage.setVisibility(VISIBLE);
    }

    /**
     * 隐藏View
     */
    public void onHideView() {
        this.mPlayIcon.setVisibility(GONE);
        this.mCoverImage.setVisibility(GONE);
    }

    /**
     * 设置 拦截事件
     *
     * @param intercept True/False
     */
    public void setIntercept(boolean intercept) {
        this.mIntercept = intercept;
    }

    /**
     * 获取 拦截事件
     *
     * @return True/False
     */
    public boolean isIntercept() {
        return mIntercept;
    }

    /**
     * 更新宽度
     */
    @Keep
    public void setWidth(float width) {
        this.getLayoutParams().width = (int) width;
        this.requestLayout();
    }

    /**
     * 更新高度
     */
    @Keep
    public void setHeight(float height) {
        this.getLayoutParams().height = (int) height;
        this.requestLayout();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 拦截所有事件
        if (isIntercept()) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
