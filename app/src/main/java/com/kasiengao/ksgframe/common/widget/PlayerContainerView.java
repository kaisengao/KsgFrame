package com.kasiengao.ksgframe.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

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
        // 封面图
        GlideUtil.loadImage(getContext(), imageUrl, mCoverImage);
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
     * 设置 拦截事件
     *
     * @param intercept True/False
     */
    public void setIntercept(boolean intercept) {
        this.mIntercept = intercept;
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
