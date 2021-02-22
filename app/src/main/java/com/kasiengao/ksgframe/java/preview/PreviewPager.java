package com.kasiengao.ksgframe.java.preview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kasiengao.base.util.CommonUtil;
import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.base.util.StatusBarUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.observer.MyLifecycleObserver;
import com.kasiengao.ksgframe.java.player.cover.ControllerCover;
import com.kasiengao.ksgframe.java.player.cover.GestureCover;
import com.kasiengao.ksgframe.java.player.cover.LoadingCover;
import com.kasiengao.ksgframe.java.player.player.KsgIjkPlayer;
import com.kasiengao.ksgframe.java.util.AnimUtil;
import com.kasiengao.ksgframe.java.util.SystemUiUtil;
import com.kasiengao.ksgframe.java.widget.PlayerContainerView;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.assist.InterEvent;
import com.ksg.ksgplayer.assist.OnAssistViewEventHandler;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.receiver.ReceiverGroup;
import com.ksg.ksgplayer.widget.KsgAssistView;

import java.util.List;

/**
 * @ClassName: PreviewPager
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/20 10:43
 * @Description: 自适应 预览 Banner
 */
public class PreviewPager<T extends IPreviewParams> extends FrameLayout implements ViewPager.OnPageChangeListener {

    private int mCurrentPosition;

    private int mDataSize = 0;

    private int mNormalHeight = 0;

    private boolean mUserPause;

    private boolean mFullscreen;

    private boolean mIsLandscape;

    private List<T> mMediaList;

    private Activity mActivity;

    private ViewPagerEx mViewPager;

    private KsgAssistView mKsgAssistView;

    private ReceiverGroup mReceiverGroup;

    private AppCompatTextView mPagerCount;

    private AppCompatImageView mDefaultImage;

    private PlayerContainerView mPlayerContainer;

    private PreviewPagerAdapter<T> mPagerAdapter;

    private MyLifecycleObserver mLifecycleObserver;

    public PreviewPager(@NonNull Context context) {
        this(context, null);
    }

    public PreviewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mActivity = CommonUtil.scanForActivity(context);
        // Init View
        this.initView();
        // Init Lifecycle
        this.initLifecycle();
    }

    /**
     * Init View
     */
    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_preview_pager, this, true);
        this.mViewPager = inflate.findViewById(R.id.layout_preview_pager);
        this.mViewPager.addOnPageChangeListener(this);
        this.mViewPager.setAdapter(mPagerAdapter = new PreviewPagerAdapter<>());
        this.mPagerCount = inflate.findViewById(R.id.layout_pager_count);
        this.mDefaultImage = inflate.findViewById(R.id.layout_preview_default);

        FrameLayout toolbar = inflate.findViewById(R.id.layout_toolbar);
        StatusBarUtil.setPaddingSmart(getContext(), toolbar);
    }

    /**
     * Init Lifecycle
     */
    private void initLifecycle() {
        this.mLifecycleObserver = new MyLifecycleObserver(mActivity) {

            @Override
            protected void onAcResume() {
                if (mKsgAssistView != null) {
                    if (mKsgAssistView.getVideoPlayer().isInPlaybackState()) {
                        if (!mUserPause) {
                            mKsgAssistView.resume();
                        }
                    }
                }
                if (mIsLandscape) {
                    // 隐藏系统Ui
                    SystemUiUtil.hideVideoSystemUI(getContext());
                }
            }

            @Override
            protected void onAcPause() {
                if (mKsgAssistView != null) {
                    if (mKsgAssistView.getVideoPlayer().isInPlaybackState()) {
                        mKsgAssistView.pause();
                    } else {
                        mKsgAssistView.stop();
                    }
                }
            }

            @Override
            protected void onAcDestroy() {
                if (mKsgAssistView != null) {
                    mKsgAssistView.destroy();
                    mKsgAssistView = null;
                }
                mViewPager.removeOnPageChangeListener(PreviewPager.this);
                mLifecycleObserver.removeObserver();
                mLifecycleObserver = null;
            }
        };
        // addObserver
        this.mLifecycleObserver.addObserver();
    }

    /**
     * initAssistVideo
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private void initAssistVideo() {
        if (this.mKsgAssistView == null) {
            this.mKsgAssistView = new KsgAssistView(getContext());
            this.mKsgAssistView.setDecoderView(new KsgIjkPlayer(getContext()));
            this.mKsgAssistView.getVideoPlayer().getKsgContainer().setBackgroundColor(Color.BLACK);

            this.mReceiverGroup = new ReceiverGroup();
            this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER, new ControllerCover(getContext()));
            this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_LOADING_COVER, new LoadingCover(getContext()));
            this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER, new GestureCover(getContext()));

            this.mKsgAssistView.getVideoPlayer().setReceiverGroup(mReceiverGroup);
            this.mKsgAssistView.getVideoPlayer().setBaseEventAssistHandler(new OnAssistViewEventHandler(mKsgAssistView){
                @Override
                public void onAssistHandle(int eventCode, Bundle bundle) {
                    super.onAssistHandle(eventCode, bundle);
                    switch (eventCode) {
                        case InterEvent.CODE_REQUEST_PAUSE:
                            mUserPause = true;
                            break;
                        case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                            // 回退
                            mActivity.onBackPressed();
                            break;
                        case DataInter.Event.EVENT_CODE_REQUEST_SCREEN_ORIENTATION:
                            // 横竖屏切换
                            boolean screenOrientation = bundle.getBoolean(EventKey.BOOL_DATA, false);
                            // 改变横竖屏
                            setRequestedOrientation(screenOrientation
                                    ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    // 横屏自动旋转 180°
                                    : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                            break;
                        case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                            // 全屏切换事件
                            boolean fullscreen = bundle.getBoolean(EventKey.BOOL_DATA, false);
                            // 如果在横屏状态下退出了全屏模式需要设置回竖屏
                            if (mIsLandscape && !fullscreen) {
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }
                            // 存储变量
                            setFullscreen(fullscreen);
                            // 屏幕改变
                            onFullscreen(fullscreen);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    /**
     * 配置 媒体数据列表
     *
     * @param mediaList mediaList
     */
    public void setMediaList(List<T> mediaList) {
        if (mediaList == null || mediaList.isEmpty()) {
            this.mDefaultImage.setVisibility(VISIBLE);
            return;
        }
        this.mDefaultImage.setVisibility(GONE);
        // 数据列表长度
        this.mDataSize = mediaList.size();
        // 媒体数据列表
        this.mMediaList = mediaList;
        // 默认position
        this.mCurrentPosition = 0;
        // 默认以第一张图的宽高为准
        this.setLayoutParams(mMediaList.get(mCurrentPosition));
        // 配置 ViewPager 页数
        this.setCurrentCount();
        // 配置 Data
        this.mPagerAdapter.setMediaList(mMediaList);
        // 默认页面
        this.mViewPager.post(() -> this.onPageSelected(mCurrentPosition));
        // 如果只有一条数据 则关闭滑动
        this.mViewPager.setScrollable(mDataSize > 1);
    }

    /**
     * 配置 ViewPager 的宽高
     *
     * @param pagerParams pagerParams
     */
    private void setLayoutParams(IPreviewParams pagerParams) {
        // 等比例缩放宽高
        int[] screenSize = DensityUtil.scaleScreenSize(getContext(), pagerParams.getWidth(), pagerParams.getHeight(), 1);
        this.mNormalHeight = screenSize[1];
        this.getLayoutParams().width = screenSize[0];
        this.getLayoutParams().height = screenSize[1];
        this.setLayoutParams(getLayoutParams());
    }

    /**
     * 配置 ViewPager 页数
     */
    private void setCurrentCount() {
        this.mPagerCount.setText(String.format(getContext().getString(R.string.preview_count), (mCurrentPosition + 1), mDataSize));
    }

    /**
     * 配置 播放器容器
     *
     * @param playerContainer PlayerContainerView
     */
    public void setPlayerContainer(PlayerContainerView playerContainer) {
        this.mPlayerContainer = playerContainer;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 验证 最后一个item不作处理
        if ((mPagerAdapter.getCount() - 1) == position) {
            return;
        }
        // 高度数据
        int[] itemHeight = mPagerAdapter.getItemHeight();
        // 动态配置ViewPager高度达到自适应效果
        this.getLayoutParams().height = (int) ((itemHeight[position] == 0 ? mNormalHeight : itemHeight[position]) * (1 - positionOffset) +
                (itemHeight[position + 1] == 0 ? mNormalHeight : itemHeight[position + 1]) * positionOffset);
        this.requestLayout();
    }

    @Override
    public void onPageSelected(int position) {
        this.mCurrentPosition = position;
        // 播放Item
        this.playPosition(position);
        // 配置 ViewPager 页数
        this.setCurrentCount();
        // 设置 播放器容器的宽度与ViewPager高度保持一致
        this.mPlayerContainer.getLayoutParams().height = mPagerAdapter.getItemHeight()[position];
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 播放视频
     *
     * @param position 位置
     */
    private void playPosition(int position) {
        T pagerParams = mMediaList.get(position);
        // 不管图片还是视频只要发生改变 停止就完了
        if (this.mKsgAssistView != null) {
            this.mKsgAssistView.stop();
        }
        // container
        FrameLayout container = mViewPager.findViewWithTag(position);
        // 类型区分
        if (container != null && "video".equals(pagerParams.getMediaType())) {
            // 初始化辅助播放器
            this.initAssistVideo();
            // 添加容器 播放
            this.mKsgAssistView.attachContainer(container, true);
            this.mKsgAssistView.setDataSource(new DataSource(pagerParams.getVideoUrl()));
            this.mKsgAssistView.start();
            this.mKsgAssistView.getVideoPlayer().setLooping(true);
        }
    }

    /**
     * 屏幕改变
     *
     * @param fullscreen 屏幕状态
     */
    public void onFullscreen(boolean fullscreen) {
        // 播放器
        if (mKsgAssistView != null) {
            // 拦截事件
            this.mPlayerContainer.setIntercept(fullscreen);
            // 全屏
            if (fullscreen) {
                // 更换播放器的容器
                this.mKsgAssistView.attachContainer(mPlayerContainer, false);
                // 全屏动画 开启全屏
                this.onFullScreenAnim(true, null);
            } else {
                // 全屏动画 结束全屏
                this.onFullScreenAnim(false, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // container
                        FrameLayout container = mViewPager.findViewWithTag(mViewPager.getCurrentItem());
                        // 更换播放器的容器
                        if (container != null) {
                            mKsgAssistView.attachContainer(container, false);
                        }
                    }
                });
            }
            // 通知组件横屏幕改变
            this.mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_FULLSCREEN, fullscreen);
        }
    }

    /**
     * 全屏动画
     */
    private void onFullScreenAnim(boolean fullscreen, AnimatorListenerAdapter animatorListenerAdapter) {
        // 获取View在屏幕上的高度位置
        int viewHeight = mPagerAdapter.getItemHeight()[mViewPager.getCurrentItem()];
        int screenHeight = (int) DensityUtil.getHeightInPx(getContext());
        // 全屏动画
        AnimUtil.fullScreenAnim(mPlayerContainer, fullscreen, viewHeight, screenHeight, animation -> {
            // 更新高度
            this.mPlayerContainer.getLayoutParams().height = (int) animation.getAnimatedValue();
            this.mPlayerContainer.requestLayout();
        }, animatorListenerAdapter);
    }

    /**
     * 获取一下屏幕改变事件
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 验证是否横屏状态
        this.mIsLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        // 横屏
        if (mIsLandscape) {
            // 如果直接选择的横屏且屏幕不在全屏状态下 默认设置横屏且全屏
            if (!mFullscreen) {
                // 改为全屏布局
                this.onFullscreen(true);
            }
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(getContext());
        } else {
            // 竖屏
            this.onFullscreen(mFullscreen);
            // 恢复系统Ui
            SystemUiUtil.recoverySystemUI(getContext());
        }
        // 通知组件横竖屏切换
        this.mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_SCREEN_ORIENTATION, mIsLandscape);
    }

    /**
     * 设置横竖屏
     *
     * @param requestedOrientation requestedOrientation
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public void setRequestedOrientation(int requestedOrientation) {
        this.mActivity.setRequestedOrientation(requestedOrientation);
    }

    public void setFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;
    }

    public boolean isFullscreen() {
        return mFullscreen;
    }

    public boolean isLandscape() {
        return mIsLandscape;
    }

    /**
     * Adapter
     *
     * @param <T> {@link IPreviewParams}
     */
    static class PreviewPagerAdapter<T extends IPreviewParams> extends PagerAdapter {

        private int[] mItemHeight;

        private List<T> mMediaList;

        void setMediaList(List<T> mediaList) {
            this.mMediaList = mediaList;
            // ItemHeight 高度数组
            this.mItemHeight = null;
            this.mItemHeight = new int[getCount()];
            // notifyData
            this.notifyDataSetChanged();
        }

        int[] getItemHeight() {
            return this.mItemHeight;
        }

        @Override
        public int getCount() {
            return this.mMediaList == null ? 0 : mMediaList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            T pagerParams = this.mMediaList.get(position);
            // RootView
            View itemView = View.inflate(container.getContext(), R.layout.layout_preview_item, null);
            // 预览图
            SimpleDraweeView imageView = itemView.findViewById(R.id.item_preview_image);
            // 等比例缩放宽高
            int[] screenSize = DensityUtil.scaleScreenSize(container.getContext(), pagerParams.getWidth(), pagerParams.getHeight(), 1);
            // 存储高度
            this.mItemHeight[position] = screenSize[1];
            // LoadImage
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setUri(pagerParams.getPictureUrl())
                    // 设置加载图片完成后是否直接进行播放
                    .setAutoPlayAnimations(true)
                    .build();
            imageView.setController(draweeController);
            // 类型区分
            if ("video".equals(pagerParams.getMediaType())) {
                // 播放器容器
                FrameLayout playerContainer = itemView.findViewById(R.id.item_preview_player);
                playerContainer.setTag(position);
            }
            // ViewPager 视图添加
            container.addView(itemView);
            // Return
            return itemView;
        }
    }
}
