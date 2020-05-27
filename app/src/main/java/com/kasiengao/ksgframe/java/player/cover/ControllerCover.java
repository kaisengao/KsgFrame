package com.kasiengao.ksgframe.java.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.kasiengao.base.configure.ThreadPool;
import com.kasiengao.base.util.TimeUtil;
import com.kasiengao.ksgframe.R;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.receiver.BaseCover;
import com.ksg.ksgplayer.receiver.IReceiverGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @ClassName: ControllerCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 17:10
 * @Description: 控制器 Cover
 */
public class ControllerCover extends BaseCover implements OnTimerUpdateListener {

    private Unbinder mBind;

    @BindView(R.id.cover_controller_seek)
    AppCompatSeekBar mSeekBar;
    @BindView(R.id.cover_controller_play_status)
    AppCompatImageView mPlayStatus;
    @BindView(R.id.cover_controller_fullscreen_status)
    AppCompatImageView mFullscreenStatus;
    @BindView(R.id.cover_controller_curr_time)
    AppCompatTextView mCurrTime;
    @BindView(R.id.cover_controller_duration_time)
    AppCompatTextView mDurationTime;

    private long mBufferPercentage;

    private String mTimeFormat;

    private boolean mTimerUpdatePause = false;

    private ThreadPool.MainThreadHandler mHandler;

    public ControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_controller, null);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        this.mBind = ButterKnife.bind(this, getView());
        // Handler
        this.mHandler = ThreadPool.MainThreadHandler.getInstance();
        // SeekBar
        this.mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        // 组件间通信
        this.getGroupValue().registerOnGroupValueUpdateListener(mGroupValueUpdateListener);
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
        this.mBind.unbind();
        // 组件间通信
        this.getGroupValue().unregisterOnGroupValueUpdateListener(mGroupValueUpdateListener);
    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                // 新的播放地址 初始化数据
                this.mTimeFormat = null;
                this.mBufferPercentage = 0;
                this.onRenewUi(0, 0);
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                // 播放状态改变
                int status = bundle.getInt(EventKey.INT_DATA);
                if (status == IKsgPlayer.STATE_PAUSED) {
                    this.mPlayStatus.setSelected(true);
                } else if (status == IKsgPlayer.STATE_STARTED) {
                    this.mPlayStatus.setSelected(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    /**
     * @param curr             当前进度
     * @param duration         总进度
     * @param bufferPercentage 缓冲进度
     */
    @Override
    public void onTimerUpdate(long curr, long duration, long bufferPercentage) {
        // 验证是否暂停了进度更新
        if (mTimerUpdatePause) {
            return;
        }
        // 缓冲进度
        this.mBufferPercentage = bufferPercentage;
        // 时长格式 00：00 or 00:00:00
        if (TextUtils.isEmpty(mTimeFormat) || duration != mSeekBar.getMax()) {
            this.mTimeFormat = TimeUtil.getFormat(duration);
        }
        // 更新Ui
        this.onRenewUi(curr, duration);
    }

    @OnClick({R.id.cover_controller_play_status, R.id.cover_controller_fullscreen_status})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.cover_controller_play_status:
                // 播放状态
                this.onSwitchPlayState();
                break;
            case R.id.cover_controller_fullscreen_status:
                // 全屏状态
                this.notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN, null);
                break;
            default:
                break;
        }
    }

    /**
     * 组件间通信
     */
    private IReceiverGroup.OnGroupValueUpdateListener mGroupValueUpdateListener = new IReceiverGroup.OnGroupValueUpdateListener() {
        @Override
        public String[] filterKeys() {
            return new String[]{
                    DataInter.Key.KEY_IS_LANDSCAPE
            };
        }

        @Override
        public void onValueUpdate(String key, Object value) {
            switch (key) {
                case DataInter.Key.KEY_IS_LANDSCAPE:
                    // 横竖屏切换
                    mFullscreenStatus.setSelected((boolean) value);
                    break;
            }
        }
    };

    /**
     * 播放状态
     */
    private void onSwitchPlayState() {
        boolean selected = mPlayStatus.isSelected();
        if (selected) {
            this.requestResume(null);
        } else {
            this.requestPause(null);
        }
        mPlayStatus.setSelected(!selected);
    }

    /**
     * 更新Ui
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    private void onRenewUi(long curr, long duration) {
        // 更新进度
        this.setSeekProgress(curr, duration);
        this.setSeekProgressTime(curr, duration);
    }

    /**
     * 设置 播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    private void setSeekProgress(long curr, long duration) {
        this.mSeekBar.setMax((int) duration);
        this.mSeekBar.setProgress((int) curr);
        float secondProgress = mBufferPercentage * 1.0f / 100 * duration;
        // 缓冲进度
        this.mSeekBar.setSecondaryProgress((int) secondProgress);
    }

    /**
     * 设置 播放进度Time
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    private void setSeekProgressTime(long curr, long duration) {
        this.mCurrTime.setText(TimeUtil.getTime(mTimeFormat, curr));
        this.mDurationTime.setText(TimeUtil.getTime(mTimeFormat, duration));
    }

    /**
     * Seek
     */
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                // 更新Ui
                onRenewUi(progress, seekBar.getMax());
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // 暂停进度更新
            mTimerUpdatePause = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 跳转到选中的进度
            mHandler.removeCallbacks(mSeekEventRunnable);
            mHandler.post(mSeekEventRunnable, 300);
        }
    };

    /**
     * 进度跳转
     */
    private Runnable mSeekEventRunnable = () -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_DATA, mSeekBar.getProgress());
        this.requestSeek(bundle);
        // 开放进度更新
        this.mTimerUpdatePause = false;
    };

    @Override
    public int getCoverLevel() {
        return super.levelHigh(1);
    }
}
