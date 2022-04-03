package com.kasiengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.base.util.TimeUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: ControllerCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 17:10
 * @Description: Controller
 */
public class ControllerCover extends BaseControllerCover implements OnTimerUpdateListener, View.OnClickListener {

    private View mControllerTop;

    private View mControllerBottom;

    private AppCompatSeekBar mProgress;

    private AppCompatTextView mCurrTime;

    private AppCompatTextView mDurationTime;

    private ProgressBar mBottomProgress;

    private AppCompatImageView mPlayState;

    private AppCompatImageView mLPScreenState;

    private AppCompatImageView mFullscreenState;

    public ControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_controller, null);
    }

    /**
     * InitViews
     */
    @Override
    public void initViews() {
        super.initViews();
        // Views
        this.mControllerTop = findViewById(R.id.cover_controller_top);
        this.mControllerBottom = findViewById(R.id.cover_controller_bottom);
        this.mProgress = findViewById(R.id.cover_controller_seek);
        this.mBottomProgress = findViewById(R.id.cover_controller_bottom_seek);
        this.mPlayState = findViewById(R.id.cover_controller_play);
        this.mLPScreenState = findViewById(R.id.cover_controller_lp_screen);
        this.mFullscreenState = findViewById(R.id.cover_controller_fullscreen);
        this.mCurrTime = findViewById(R.id.cover_controller_curr_time);
        this.mDurationTime = findViewById(R.id.cover_controller_duration_time);
        // OnClick
        findViewById(R.id.cover_controller_back).setOnClickListener(this);
        this.mPlayState.setOnClickListener(this);
        this.mLPScreenState.setOnClickListener(this);
        this.mFullscreenState.setOnClickListener(this);
        // SeekBar
        this.mProgress.setOnSeekBarChangeListener(this);
    }

    /**
     * 值 过滤器
     *
     * @return {"Key1","Key2","Key3"}
     */
    @Override
    public String[] getValueFilters() {
        String[] superFilters = super.getValueFilters();
        String[] valueFilters = new String[]{
                CoverConstant.ValueKey.KEY_LP_SCREEN_TOGGLE,
                CoverConstant.ValueKey.KEY_FULLSCREEN_TOGGLE,
        };
        String[] filters = new String[superFilters.length + valueFilters.length];
        System.arraycopy(superFilters, 0, filters, 0, superFilters.length);
        System.arraycopy(valueFilters, 0, filters, superFilters.length, valueFilters.length);
        return filters;
    }

    /**
     * 值 事件
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void onValueEvent(String key, Object value) {
        super.onValueEvent(key, value);
        switch (key) {
            case CoverConstant.ValueKey.KEY_LP_SCREEN_TOGGLE:
                // 横竖屏切换
                this.mLPScreenState.setSelected((boolean) value);
                break;
            case CoverConstant.ValueKey.KEY_FULLSCREEN_TOGGLE:
                // 全屏切换
                this.mFullscreenState.setSelected((boolean) value);
                break;
            default:
                break;
        }
    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        super.onPlayerEvent(eventCode, bundle);
        if (eventCode == OnPlayerListener.PLAYER_EVENT_ON_STATE_CHANGE) {
            // 播放状态改变
            this.setPlayState(bundle.getInt(EventKey.INT_DATA));
        }
    }

    /**
     * 设置状态
     */
    private void setPlayState(int state) {
        if (state == IPlayer.STATE_PAUSE) {
            this.mPlayState.setSelected(false);
        } else if (state == IPlayer.STATE_START) {
            this.mPlayState.setSelected(true);
        }
    }

    /**
     * 播放状态
     */
    @Override
    protected void onSwitchPlayState() {
        super.onSwitchPlayState();
        boolean selected = mPlayState.isSelected();
        if (selected) {
            this.requestPause(null);
        } else {
            this.requestResume(null);
        }
        this.mPlayState.setSelected(!selected);
    }

    /**
     * onClick
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cover_controller_back) {
            // Back
            this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_BACK, null);
        } else if (id == R.id.cover_controller_play) {
            // 播放状态
            this.onSwitchPlayState();
        } else if (id == R.id.cover_controller_lp_screen) {
            // 横竖屏切换
            Bundle bundle = BundlePool.obtain();
            bundle.putBoolean(EventKey.BOOL_DATA, mLPScreenState.isSelected());
            this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_LP_SCREEN_TOGGLE, bundle);
        } else if (id == R.id.cover_controller_fullscreen) {
            // 全屏切换
            Bundle bundle = BundlePool.obtain();
            bundle.putBoolean(EventKey.BOOL_DATA, !mFullscreenState.isSelected());
            this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_TOGGLE, bundle);
        }
    }

    /**
     * 跳转进度
     */
    @Override
    protected void onSeek() {
        super.onSeek();
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mProgress.getProgress());
        this.requestSeek(bundle);
    }

    /**
     * 设置 播放进度Time
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    @Override
    protected void setSeekProgressTime(long curr, long duration) {
        super.setSeekProgressTime(curr, duration);
        this.mCurrTime.setText(TimeUtil.getTimeFormatText(mTimeFormat, curr));
        this.mDurationTime.setText(TimeUtil.getTimeFormatText(mTimeFormat, duration));
    }

    /**
     * 设置 播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    @Override
    protected void setSeekProgress(long curr, long duration) {
        super.setSeekProgress(curr, duration);
        this.mProgress.setMax((int) duration);
        this.mProgress.setProgress((int) curr);
        float secondProgress = mBufferPercentage * 1.0f / 100 * duration;
        // 缓冲进度
        this.mProgress.setSecondaryProgress((int) secondProgress);
    }

    /**
     * 设置 底部播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    @Override
    protected void setBottomProgress(long curr, long duration) {
        super.setBottomProgress(curr, duration);
        this.mBottomProgress.setMax((int) duration);
        this.mBottomProgress.setProgress((int) curr);
    }

    /**
     * 控制器
     */
    @Override
    protected void onController(boolean isShow) {
        super.onController(isShow);
        this.mControllerTop.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mControllerBottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mBottomProgress.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getCoverLevel() {
        return levelMedium(10);
    }
}
