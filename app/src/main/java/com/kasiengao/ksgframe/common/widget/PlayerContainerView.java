package com.kasiengao.ksgframe.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.kaisengao.base.util.GlideUtil;
import com.kasiengao.ksgframe.R;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: PlayerContainerView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/27 16:57
 * @Description: 播放器容器 View 最高优先级
 */
public class PlayerContainerView extends FrameLayout {

    private int mPlayerState = IPlayer.STATE_IDLE;

    private boolean mIntercept;

    private ProgressBar mPlayState;

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
        this.mPlayState = controlView.findViewById(R.id.play_state);
        this.mCoverImage = controlView.findViewById(R.id.cover_image);
    }

    /**
     * 设置 封面图
     *
     * @param imageUrl imageUrl
     */
    public void setCoverImage(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            GlideUtil.loadImage(getContext(), imageUrl, mCoverImage);
            this.mCoverImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置 封面图
     *
     * @param drawable drawable
     */
    public void setCoverImage(Drawable drawable) {
        this.mCoverImage.setImageDrawable(drawable);
        this.mCoverImage.setVisibility(View.VISIBLE);
    }

    /**
     * 获取 封面图
     */
    public Drawable getCoverImage() {
        return mCoverImage.getDrawable();
    }

    /**
     * 获取 播放器状态位
     */
    public int getPlayerState() {
        return mPlayerState;
    }

    /**
     * 设置 播放器状态位
     *
     * @param playerState {@link IPlayer}
     */
    public void setPlayerState(int playerState) {
        this.mPlayerState = playerState;
        // State
        switch (playerState) {
            case IPlayer.STATE_IDLE:
            case IPlayer.STATE_PAUSE:
                this.mPlayState.setEnabled(true);
                this.mPlayState.setSelected(false);
                this.mPlayState.setVisibility(View.VISIBLE);
                break;
            case IPlayer.STATE_PREPARED:
                this.mPlayState.setEnabled(false);
                this.mPlayState.setSelected(false);
                this.mPlayState.setVisibility(View.VISIBLE);
                break;
            case IPlayer.STATE_START:
                this.mPlayState.setEnabled(false);
                this.mPlayState.setSelected(true);
                this.mPlayState.setVisibility(View.GONE);
                break;
            default:
                this.mPlayState.setEnabled(true);
                this.mPlayState.setSelected(false);
                this.mPlayState.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 获取 播放器状态位
     *
     * @return {@link IPlayer}
     */
    public ProgressBar getPlayState() {
        return mPlayState;
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
        if (mIntercept) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
