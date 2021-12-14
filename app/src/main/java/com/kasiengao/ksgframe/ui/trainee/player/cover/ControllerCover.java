package com.kasiengao.ksgframe.ui.trainee.player.cover;

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

import com.kaisengao.base.configure.ThreadPool;
import com.kaisengao.base.util.TimeUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.util.AnimUtil;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.receiver.BaseCover;
import com.ksg.ksgplayer.receiver.IReceiverGroup;

/**
 * @ClassName: ControllerCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 17:10
 * @Description: 控制器 Cover
 */
public class ControllerCover extends BaseCover implements OnTimerUpdateListener, View.OnClickListener {

    private LinearLayout mControllerTop;

    private LinearLayout mControllerBottom;

    private AppCompatSeekBar mProgress;

    private ProgressBar mBottomProgress;

    private AppCompatImageView mPlayStatus;

    private AppCompatImageView mScreenOrientation;

    private AppCompatImageView mFullscreenStatus;

    private AppCompatTextView mCurrTime;

    private AppCompatTextView mDurationTime;

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
        // Views
        this.mControllerTop = findViewById(R.id.cover_controller_top);
        this.mControllerBottom = findViewById(R.id.cover_controller_bottom);
        this.mProgress = findViewById(R.id.cover_controller_seek);
        this.mBottomProgress = findViewById(R.id.cover_controller_bottom_seek);
        this.mPlayStatus = findViewById(R.id.cover_controller_play_status);
        this.mScreenOrientation = findViewById(R.id.cover_controller_screen_orientation);
        this.mFullscreenStatus = findViewById(R.id.cover_controller_fullscreen_status);
        this.mCurrTime = findViewById(R.id.cover_controller_curr_time);
        this.mDurationTime = findViewById(R.id.cover_controller_duration_time);
        // OnClick
        this.mPlayStatus.setOnClickListener(this);
        this.mScreenOrientation.setOnClickListener(this);
        this.mFullscreenStatus.setOnClickListener(this);
        this.findViewById(R.id.cover_controller_back).setOnClickListener(this);
        // Handler
        this.mHandler = ThreadPool.MainThreadHandler.getInstance();
        // SeekBar
        this.mProgress.setOnSeekBarChangeListener(mSeekBarChangeListener);
        // 组件间通信
        this.getGroupValue().registerOnGroupValueUpdateListener(mGroupValueUpdateListener);
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
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
                DataSource dataSource = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                if (dataSource != null) {
                    // 配置 直播\点播 的样式
                    this.onBottomController(dataSource.isLive());
                }
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
        // 手势快进通知暂停自动更新
        if (eventCode == DataInter.PrivateEvent.EVENT_CODE_GESTURE_SLIDE_SEEK) {
            this.onRenewUi(bundle.getLong(EventKey.LONG_ARG1), bundle.getLong(EventKey.LONG_ARG2));
            this.mTimerUpdatePause = bundle.getBoolean(EventKey.BOOL_DATA);
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
        if (TextUtils.isEmpty(mTimeFormat) || duration != mProgress.getMax()) {
            this.mTimeFormat = TimeUtil.getFormat(duration);
        }
        // 更新Ui
        this.onRenewUi(curr, duration);
    }

    /**
     * 组件间通信
     */
    private final IReceiverGroup.OnGroupValueUpdateListener mGroupValueUpdateListener = new IReceiverGroup.OnGroupValueUpdateListener() {
        @Override
        public String[] filterKeys() {
            return new String[]{
                    DataInter.Key.KEY_SCREEN_ORIENTATION,
                    DataInter.Key.KEY_FULLSCREEN,
                    DataInter.Key.KEY_CONTROLLER_STATUS,
                    DataInter.Key.KEY_CONTROLLER_PLAY_STATUS
            };
        }

        @Override
        public void onValueUpdate(String key, Object value) {
            switch (key) {
                case DataInter.Key.KEY_SCREEN_ORIENTATION:
                    boolean isLandscape = (boolean) value;
                    // 横竖屏切换
                    mScreenOrientation.setSelected(isLandscape);
                    // 隐藏 Top 顶部菜单
                    mControllerTop.setVisibility(View.GONE);
                    break;
                case DataInter.Key.KEY_FULLSCREEN:
                    // 全屏切换
                    mFullscreenStatus.setSelected((boolean) value);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cover_controller_back:
                // 回退
                this.notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_BACK, null);
                break;
            case R.id.cover_controller_play_status:
                // 播放状态
                this.onSwitchPlayStatus();
                break;
            case R.id.cover_controller_screen_orientation:
                // 横竖屏切换  (true为横屏)
                Bundle screenOrientation = BundlePool.obtain();
                screenOrientation.putBoolean(EventKey.BOOL_DATA, mScreenOrientation.isSelected());
                this.notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_SCREEN_ORIENTATION, screenOrientation);
                break;
            case R.id.cover_controller_fullscreen_status:
                // 全屏切换状态 (true为全屏)
                Bundle fullscreen = BundlePool.obtain();
                fullscreen.putBoolean(EventKey.BOOL_DATA, !mFullscreenStatus.isSelected());
                this.notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN, fullscreen);
                break;
            default:
                break;
        }
    }

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
        this.setControllerBottomStatus(mControllerBottom, mControllerStatus);
        // Bottom 底部进度条
        this.setControllerBottomStatus(mBottomProgress, !mControllerStatus);
    }

    /**
     * Top 顶部菜单
     *
     * @param status 状态
     */
    private void setControllerTopStatus(boolean status) {
        // 横屏状态下才开放
        if (mScreenOrientation.isSelected()) {
            // 区分一下状态
            int visibility = status ? View.VISIBLE : View.GONE;
            // 过滤一下重复动画
            if (mControllerTop.getVisibility() == visibility) {
                return;
            }
            // 赋值并执行动画
            this.mControllerTop.setVisibility(visibility);
            this.mControllerTop.setAnimation(status
                    ? AnimUtil.getTopInAnim(getContext())
                    : AnimUtil.getTopOutAnim(getContext()));
        }
    }

    /**
     * Bottom 底部菜单
     *
     * @param bottomView View
     * @param status     状态
     */
    private void setControllerBottomStatus(View bottomView, boolean status) {
        // 区分一下状态
        int visibility = status ? View.VISIBLE : View.GONE;
        // 过滤一下重复动画
        if (bottomView.getVisibility() == visibility) {
            return;
        }
        // 赋值并执行动画
        bottomView.setVisibility(visibility);
        bottomView.setAnimation(status
                ? AnimUtil.getBottomInAnim(getContext())
                : AnimUtil.getBottomOutAnim(getContext()));
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
        this.mPlayStatus.setSelected(!selected);
    }

    /**
     * 配置 直播\点播 的样式
     *
     * @param isLive 直播\点播
     */
    private void onBottomController(boolean isLive) {
        this.mCurrTime.setVisibility(!isLive ? View.VISIBLE : View.INVISIBLE);
        this.mDurationTime.setVisibility(!isLive ? View.VISIBLE : View.INVISIBLE);
        this.mProgress.setVisibility(!isLive ? View.VISIBLE : View.INVISIBLE);
        this.mBottomProgress.setVisibility(!isLive ? View.VISIBLE : View.INVISIBLE);
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
    private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

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
    private final Runnable mControllerStatusRunnable = () -> {
        this.mControllerStatus = false;
        // Top/Bottom 菜单
        this.setControllerTopStatus(false);
        this.setControllerBottomStatus(mControllerBottom, false);
        // Bottom 底部进度条
        this.setControllerBottomStatus(mBottomProgress, true);
    };

    /**
     * 进度跳转
     */
    private final Runnable mSeekEventRunnable = () -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mProgress.getProgress());
        this.requestSeek(bundle);
        // 开放进度更新
        this.mTimerUpdatePause = false;
    };

    @Override
    public int getCoverLevel() {
        return levelHigh(10);
    }
}
