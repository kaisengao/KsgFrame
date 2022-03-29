package com.kasiengao.ksgframe.player;

import androidx.annotation.NonNull;

import com.kaisengao.base.BaseApplication;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.kasiengao.ksgframe.factory.AppFactory;
import com.kasiengao.ksgframe.player.cover.LoadingCover;
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
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_LOADING,new LoadingCover(application));
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
                case CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_TOGGLE:
                    // 切换全屏
                    this.onFullscreen();
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
     */
    public void bindNewContainer(PlayerContainerView container) {
        // 同步状态位
        if (getCurrContainer() != null) {
            container.setPlayerState(getCurrContainer().getPlayerState());
        }
        // 修改当前容器
        this.setCurrContainer(container);
        // 重新绑定容器
        this.getPlayer().bindContainer(container);
    }

    /**
     * 播放
     *
     * @param position  当前位置
     * @param videoUrl  播放地址
     * @param container 新的容器
     */
    public void onPlay(int position, String videoUrl, PlayerContainerView container) {
        // 重置当前容器
        this.resetCurrContainer();
        // 标记当前位置与容器
        this.setCurrPosition(position);
        this.setCurrContainer(container);
        // 设置状态
        container.setPlayerState(IPlayer.STATE_PREPARED);
        // 播放视频
        KsgAssistView player = getPlayer();
        player.unbindContainer();
        player.setDataSource(new DataSource(videoUrl));
        player.start();
    }

    /**
     * 继续
     */
    public void onResume() {
        PlayerContainerView currContainer = getCurrContainer();
        if (currContainer != null) {
            // 设置状态
            currContainer.setIntercept(true);
            currContainer.setPlayerState(IPlayer.STATE_START);
            // 继续播放
            KsgAssistView player = getPlayer();
            player.bindContainer(currContainer);
            if (!mUserPause) {
                player.resume();
            }
        }
    }

    /**
     * 暂停
     */
    public void onPause() {
        PlayerContainerView currContainer = getCurrContainer();
        if (currContainer != null) {
            // 设置状态
            currContainer.setIntercept(false);
            currContainer.setPlayerState(IPlayer.STATE_PAUSE);
            // 暂停播放
            KsgAssistView player = getPlayer();
            player.unbindContainer();
            player.pause();
        }
    }


    /**
     * 全屏
     */
    private void onFullscreen() {
        PlayerContainerView currContainer = getCurrContainer();
        if (currContainer != null && mListPlayerListener != null) {
            // Listener
            this.mListPlayerListener.onFullscreen(currContainer);
        }
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
         * 全屏
         *
         * @param listContainer 当前容器
         */
        void onFullscreen(@NonNull PlayerContainerView listContainer);
    }
}