package com.kasiengao.ksgframe.java.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.kasiengao.base.configure.ThreadPool;
import com.kasiengao.base.util.TimeUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.util.AnimUtil;
import com.kasiengao.ksgframe.java.util.SystemUiUtil;
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

    @BindView(R.id.cover_controller_top)
    LinearLayout mControllerTop;
    @BindView(R.id.cover_controller_bottom)
    LinearLayout mControllerBottom;
    @BindView(R.id.cover_controller_seek)
    AppCompatSeekBar mSeekBar;
    @BindView(R.id.cover_controller_bottom_seek)
    ProgressBar mBottomProgress;
    @BindView(R.id.cover_controller_play_status)
    AppCompatImageView mPlayStatus;
    @BindView(R.id.cover_controller_volume_status)
    AppCompatImageView mVolumeStatus;
    @BindView(R.id.cover_controller_fullscreen_status)
    AppCompatImageView mFullscreenStatus;
    @BindView(R.id.cover_controller_curr_time)
    AppCompatTextView mCurrTime;
    @BindView(R.id.cover_controller_duration_time)
    AppCompatTextView mDurationTime;

    private long mBufferPercentage;

    private String mTimeFormat;

    private boolean mControllerStatus = true;

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
        // 计时 菜单状态 停止
        this.onRemoveDelayedControllerStatus();
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
            case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                // 视频准备完毕
                // 验证Controller是否在显示状态中
                if (mControllerStatus) {
                    // 计时 菜单状态 开始
                    onDelayedControllerStatus();
                }
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                // 播放状态改变
                int status = bundle.getInt(EventKey.INT_DATA);
                if (status == IKsgPlayer.STATE_PAUSED) {
                    this.mPlayStatus.setSelected(false);
                } else if (status == IKsgPlayer.STATE_STARTED) {
                    this.mPlayStatus.setSelected(true);
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

    @Nullable
    @Override
    public Bundle onPrivateEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case DataInter.PrivateEvent.EVENT_CODE_GESTURE_SLIDE_SEEK:
                // 手势快进通知暂停自动更新
                this.onRenewUi(bundle.getLong(EventKey.LONG_ARG1), bundle.getLong(EventKey.LONG_ARG2));
                this.mTimerUpdatePause = bundle.getBoolean(EventKey.BOOL_DATA);
                break;
        }
        return super.onPrivateEvent(eventCode, bundle);
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

    @OnClick({R.id.cover_controller_back, R.id.cover_controller_play_status
            , R.id.cover_controller_fullscreen_status, R.id.cover_controller_volume_status})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.cover_controller_back:
                // 回退
                this.notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_BACK, null);
                break;
            case R.id.cover_controller_play_status:
                // 播放状态
                this.onSwitchPlayStatus();
                break;
            case R.id.cover_controller_fullscreen_status:
                // 全屏状态
                this.notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN, null);
                break;
            case R.id.cover_controller_volume_status:
                // 声音状态
                Bundle obtain = BundlePool.obtain();
                obtain.putBoolean(EventKey.BOOL_DATA, mVolumeStatus.isSelected());
                this.notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_VOLUME_ALTER, obtain);
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
                    DataInter.Key.KEY_IS_LANDSCAPE,
                    DataInter.Key.KEY_VOLUME_ALTER,
                    DataInter.Key.KEY_CONTROLLER_STATUS,
                    DataInter.Key.KEY_CONTROLLER_PLAY_STATUS
            };
        }

        @Override
        public void onValueUpdate(String key, Object value) {
            switch (key) {
                case DataInter.Key.KEY_IS_LANDSCAPE:
                    boolean isLandscape = (boolean) value;
                    // 横竖屏切换
                    mFullscreenStatus.setSelected(isLandscape);
                    // SystemUi
                    setSystemUiStatus(isLandscape);
                    // Top 顶部菜单
                    setControllerTopStatus(isLandscape);
                    break;
                case DataInter.Key.KEY_VOLUME_ALTER:
                    // 声音开关事件
                    mVolumeStatus.setSelected((boolean) value);
                    break;
                case DataInter.Key.KEY_CONTROLLER_STATUS:
                    // Controller 状态事件
                    mControllerStatus = !mControllerStatus;
                    // Top/Bottom 菜单
                    onControllerStatus();
                    break;
                case DataInter.Key.KEY_CONTROLLER_PLAY_STATUS:
                    // Controller 播放状态
                    onSwitchPlayStatus();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 计时 菜单状态 开始
     */
    private void onDelayedControllerStatus() {
        this.onRemoveDelayedControllerStatus();
        this.mHandler.post(mControllerStatusRunnable, 8000);
    }

    /**
     * 计时 菜单状态 停止
     */
    private void onRemoveDelayedControllerStatus() {
        this.mHandler.removeCallbacks(mControllerStatusRunnable);
    }

    /**
     * Top/Bottom 菜单
     */
    private void onControllerStatus() {
        // SystemUi 全屏状态下 且 需要隐藏Ui
        if (this.mFullscreenStatus.isSelected() && !mControllerStatus) {
            SystemUiUtil.hideVideoSystemUI(getContext());
        }
        // 验证Controller是否在显示状态中
        if (this.mControllerStatus) {
            // 计时 菜单状态 开始
            this.onDelayedControllerStatus();
        } else {
            // 计时 菜单状态 停止
            this.onRemoveDelayedControllerStatus();
        }
        // Top 菜单  (必须在横屏状态下才会显示)
        this.setControllerTopStatus(mControllerStatus);
        // Bottom 菜单
        this.setControllerBottomStatus(mControllerStatus);
    }

    /**
     * 系统 Ui
     *
     * @param isLandscape 横竖屏
     */
    private void setSystemUiStatus(boolean isLandscape) {
        if (isLandscape) {
            SystemUiUtil.hideVideoSystemUI(getContext());
        } else {
            SystemUiUtil.recoverySystemUI(getContext());
        }
    }

    /**
     * Top 顶部菜单
     *
     * @param status 状态
     */
    private void setControllerTopStatus(boolean status) {
        // 非横屏下隐藏
        if (!mFullscreenStatus.isSelected()) {
            this.mControllerTop.setVisibility(View.GONE);
            return;
        }
        if (status) {
            this.mControllerTop.setVisibility(View.VISIBLE);
            this.mControllerTop.setAnimation(AnimUtil.getTopInAnim(getContext()));
        } else {
            this.mControllerTop.setVisibility(View.GONE);
            this.mControllerTop.setAnimation(AnimUtil.getTopOutAnim(getContext()));
        }
    }

    /**
     * Bottom 底部菜单
     *
     * @param status 状态
     */
    private void setControllerBottomStatus(boolean status) {
        if (status) {
            this.mBottomProgress.setVisibility(View.GONE);
            this.mControllerBottom.setVisibility(View.VISIBLE);
            this.mControllerBottom.setAnimation(AnimUtil.getBottomInAnim(getContext()));
        } else {
            this.mBottomProgress.setVisibility(View.VISIBLE);
            this.mControllerBottom.setVisibility(View.GONE);
            this.mControllerBottom.setAnimation(AnimUtil.getBottomOutAnim(getContext()));
        }
    }

    /**
     * 播放状态
     */
    private void onSwitchPlayStatus() {
        boolean selected = mPlayStatus.isSelected();
        if (selected) {
            this.requestPause(null);
        } else {
            this.requestResume(null);
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
        this.setSeekProgressTime(curr, duration);
        this.setSeekProgress(curr, duration);
        this.setBottomProgress(curr, duration);
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
     * 设置 底部播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    private void setBottomProgress(long curr, long duration) {
        this.mBottomProgress.setMax((int) duration);
        this.mBottomProgress.setProgress((int) curr);
        float secondProgress = mBufferPercentage * 1.0f / 100 * duration;
        // 缓冲进度
        this.mBottomProgress.setSecondaryProgress((int) secondProgress);
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
     * 控制器状态
     */
    private Runnable mControllerStatusRunnable = () -> {
        this.mControllerStatus = false;
        // Top/Bottom 菜单
        this.setControllerTopStatus(false);
        this.setControllerBottomStatus(false);
    };

    /**
     * 进度跳转
     */
    private Runnable mSeekEventRunnable = () -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mSeekBar.getProgress());
        this.requestSeek(bundle);
        // 开放进度更新
        this.mTimerUpdatePause = false;
    };

    @Override
    public int getCoverLevel() {
        return super.levelHigh(10);
    }
}
