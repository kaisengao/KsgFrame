package com.kasiengao.ksgframe.ui.main.player;

import androidx.annotation.NonNull;

import com.kaisengao.base.BaseApplication;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.kasiengao.ksgframe.factory.AppFactory;
import com.kasiengao.ksgframe.player.KsgExoPlayer;
import com.kasiengao.ksgframe.player.cover.LoadingCover;
import com.kasiengao.ksgframe.ui.main.bean.VideoBean;
import com.ksg.ksgplayer.cover.CoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.widget.KsgAssistView;

/**
 * @ClassName: ListPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/27 14:51
 * @Description: 列表播放器
 */
public class ListPlayer {

    private static ListPlayer instance;

    private int mCurrPosition = -1;

    private boolean mUserPause;

    private boolean mFullscreen;

    private boolean mOperable = true;

    private boolean mHideContainer;

    private VideoBean mCurrVideoBean;

    private PlayerContainerView mCurrContainer;

    private KsgAssistView mPlayer;

    private CoverManager mCoverManager;

    private OnListPlayerListener mListPlayerListener;

    public static ListPlayer getInstance() {
        if (instance == null) {
            synchronized (ListPlayer.class) {
                if (instance == null) {
                    instance = new ListPlayer();
                }
            }
        }
        return instance;
    }

    private ListPlayer() {
        // Init Player
        this.initPlayer();
    }

    /**
     * Init Player
     */
    private void initPlayer() {
        BaseApplication application = AppFactory.application();
        this.mPlayer = new KsgAssistView(application);
        this.mPlayer.setDecoderView(new KsgExoPlayer(application));
        this.mPlayer.setBackgroundColor(R.color.black);
        // 创建 Cover管理器
        this.mCoverManager = new CoverManager();
        // Loading
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_LOADING, new LoadingCover(application));
        // 设置 Cover管理器
        this.mPlayer.setCoverManager(mCoverManager);
        // Cover事件
        this.mPlayer.setCoverEventListener((eventCode, bundle) -> {
            switch (eventCode) {
                case CoverConstant.CoverEvent.CODE_REQUEST_RESUME:
                    // 标记恢复
                    this.mUserPause = false;
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_PAUSE:
                    // 标记暂停
                    this.mUserPause = true;
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_BACK:
                    // Back
                    if (mListPlayerListener != null) {
                        mListPlayerListener.onBack();
                    }
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_ENTER:
                    // 进入全屏
                    this.onFullscreen(true);
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_EXIT:
                    // 退出全屏
                    this.onFullscreen(false);
                    break;
                default:
                    break;
            }
        });
        // 播放事件
        this.mPlayer.setPlayerListener((eventCode, bundle) -> {
            if (eventCode == OnPlayerListener.PLAYER_EVENT_ON_PREPARED) {
                PlayerContainerView currContainer = getCurrContainer();
                if (currContainer != null) {
                    // 设置状态
                    currContainer.setIntercept(true);
                    currContainer.setPlayerState(IPlayer.STATE_START);
                    // 绑定容器
                    mPlayer.bindContainer(currContainer);
                }
            }
        });
    }

    /**
     * 返回 播放器对象
     *
     * @return {@link KsgAssistView}
     */
    @NonNull
    public KsgAssistView getPlayer() {
        return mPlayer;
    }

    /**
     * 获取 Cover管理器
     */
    @NonNull
    public CoverManager getCoverManager() {
        return mCoverManager;
    }

    /**
     * 事件
     *
     * @param listPlayerListener listPlayerListener
     */
    public void setListPlayerListener(OnListPlayerListener listPlayerListener) {
        this.mListPlayerListener = listPlayerListener;
    }

    /**
     * 设置 当前位置
     *
     * @param currPosition position
     */
    private void setCurrPosition(int currPosition) {
        this.mCurrPosition = currPosition;
    }

    /**
     * 获取 当前位置
     */
    public int getCurrPosition() {
        return mCurrPosition;
    }

    /**
     * 重置当前容器
     */
    private void resetCurrContainer() {
        PlayerContainerView currContainer = getCurrContainer();
        if (currContainer != null) {
            currContainer.setIntercept(false);
            currContainer.setPlayerState(IPlayer.STATE_IDLE);
            this.setCurrContainer(null);
        }
    }

    /**
     * 设置 当前容器
     *
     * @param currContainer currContainer
     */
    private void setCurrContainer(PlayerContainerView currContainer) {
        this.mCurrContainer = currContainer;
    }

    /**
     * 获取 当前容器
     */
    public PlayerContainerView getCurrContainer() {
        return mCurrContainer;
    }

    /**
     * 绑定 新容器
     *
     * @param newContainer 新容器
     */
    public void bindNewContainer(PlayerContainerView newContainer) {
        PlayerContainerView oldContainer = getCurrContainer();
        // 同步状态位
        newContainer.setIntercept(oldContainer.isIntercept());
        newContainer.setCoverImage(oldContainer.getCoverImage());
        newContainer.setPlayerState(oldContainer.getPlayerState());
        // 修改当前容器
        this.setCurrContainer(newContainer);
        // 重新绑定容器
        if (newContainer.getPlayerState() == IPlayer.STATE_START) {
            this.getPlayer().bindContainer(newContainer);
        }
    }

    /**
     * 播放
     *
     * @param position  当前位置
     * @param videoBean 数据源
     * @param container 新的容器
     */
    public void onPlay(int position, VideoBean videoBean, PlayerContainerView container) {
        // 重置当前容器
        this.resetCurrContainer();
        // 标记当前位置与容器与数据
        this.setCurrPosition(position);
        this.setCurrContainer(container);
        this.mCurrVideoBean = videoBean;
        // 设置状态
        container.setPlayerState(IPlayer.STATE_PREPARED);
        // 播放视频
        KsgAssistView player = getPlayer();
        player.unbindContainer();
        player.setDataSource(new DataSource(videoBean.getVideoUrl()));
        player.start();
        // 配置UP主信息
        this.getCoverManager()
                .getValuePool()
                .putObject(CoverConstant.ValueKey.KEY_UPLOADER_DATA, videoBean);
    }


    /**
     * 继续
     */
    public void onResume() {
        this.mHideContainer = false;
        // 设置状态
        PlayerContainerView currContainer = getCurrContainer();
        if (currContainer != null) {
            currContainer.setIntercept(true);
            currContainer.setPlayerState(IPlayer.STATE_START);
        }
        // 绑定 视图容器
        if (mHideContainer) {
            this.getPlayer().bindContainer(currContainer);
        }
        // 继续播放
        if (!mUserPause) {
            this.getPlayer().resume();
        }

    }

    /**
     * 暂停
     */
    public void onPause() {
        this.onPause(true);
    }

    /**
     * 暂停
     */
    public void onPause(boolean hideContainer) {
        this.mHideContainer = hideContainer;
        // 设置状态
        PlayerContainerView currContainer = getCurrContainer();
        if (currContainer != null) {
            currContainer.setIntercept(false);
            currContainer.setPlayerState(IPlayer.STATE_PAUSE);
        }
        // 解绑 视图容器
        if (mHideContainer) {
            this.getPlayer().unbindContainer();
        }
        // 暂停播放
        this.getPlayer().pause();
    }

    /**
     * 设置 全屏状态
     */
    public void setFullscreen(boolean fullscreen) {
        this.mOperable = !fullscreen;
        this.mFullscreen = fullscreen;
    }

    /**
     * 全屏切换
     */
    private void onFullscreen(boolean fullscreen) {
        if (mFullscreen == fullscreen) {
            return;
        }
        // 全屏切换
        if (mListPlayerListener != null) {
            this.mListPlayerListener.onFullscreen(fullscreen);
        }
    }

    /**
     * 进入详情页
     */
    public void onEnterDetail(int position, VideoBean videoBean, PlayerContainerView container) {
        // 验证与当前播放的是否是同一个
        if (mCurrPosition == position
                && (mCurrVideoBean != null && mCurrVideoBean == videoBean)
                && (mCurrContainer != null && mCurrContainer == container)) {
            // 打开详情页
            this.onOpenDetail(position, container);
            return;
        }
        // 播放视频
        this.onPlay(position, videoBean, container);
        // 打开详情页
        this.onOpenDetail(position, container);
    }

    /**
     * 打开详情页
     */
    public void onOpenDetail() {
        this.onOpenDetail(getCurrPosition(), getCurrContainer());
    }

    /**
     * 打开详情页
     */
    private void onOpenDetail(int position, PlayerContainerView container) {
        this.mOperable = false;
        if (mListPlayerListener != null) {
            this.mListPlayerListener.onOpenDetail(position, container);
            // 通知 隐藏控制器
            this.getCoverManager()
                    .getValuePool()
                    .putObject(CoverConstant.ValueKey.KEY_HIDE_CONTROLLER, null, false);
        }
    }

    /**
     * 退出详情页
     */
    public void onExitDetail(PlayerContainerView newContainer) {
        this.mOperable = true;
        // 切回至旧容器 (原列表容器)
        ListPlayer.getInstance().bindNewContainer(newContainer);
    }

    /**
     * 是否可操作
     *
     * @return True/False
     */

    public boolean isOperable() {
        return mOperable;
    }

    /**
     * 释放
     */
    public void release() {
        // 重置当前容器
        this.resetCurrContainer();
        // 销毁资源
        if (mPlayer != null) {
            this.mPlayer.destroy();
            this.mPlayer = null;
        }
        instance = null;
    }

    public interface OnListPlayerListener {

        /**
         * Back
         */
        void onBack();

        /**
         * 打开详情页
         *
         * @param position      当前位置
         * @param listContainer 当前容器
         */
        void onOpenDetail(int position, @NonNull PlayerContainerView listContainer);

        /**
         * 全屏切换
         */
        void onFullscreen(boolean fullscreen);
    }
}