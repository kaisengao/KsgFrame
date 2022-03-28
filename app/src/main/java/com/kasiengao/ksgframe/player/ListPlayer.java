package com.kasiengao.ksgframe.player;

import com.kaisengao.base.BaseApplication;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.kasiengao.ksgframe.factory.AppFactory;
import com.kasiengao.ksgframe.player.cover.SmallControllerCover;
import com.kasiengao.ksgframe.player.cover.UploaderCover;
import com.kasiengao.ksgframe.player.entity.VideoEntity;
import com.ksg.ksgplayer.cover.CoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.widget.KsgAssistView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ListPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/27 14:51
 * @Description: 列表播放器
 */
public class ListPlayer {

    private static ListPlayer instance;

    private int mCurrPosition = -1;

    private PlayerContainerView mCurrContainer;

    private KsgAssistView mPlayer;

    private CoverManager mCoverManager;

    private final List<VideoEntity> mVideoUrls;

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
        this.mVideoUrls = new ArrayList<>();
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
        // 创建 Cover管理器
        this.mCoverManager = new CoverManager();
        // 小型控制器
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER, new SmallControllerCover(application));

        this.mCoverManager.addCover("213",new UploaderCover(application));
        // 设置 Cover管理器
        this.mPlayer.setCoverManager(mCoverManager);
        // 播放事件
        this.mPlayer.setPlayerListener((eventCode, bundle) -> {
            switch (eventCode) {
                case OnPlayerListener.PLAYER_EVENT_ON_PREPARED:
                    PlayerContainerView currContainer = getCurrContainer();
                    if (currContainer != null) {
                        // 设置状态
                        currContainer.setIntercept(true);
                        currContainer.setPlayerState(IPlayer.STATE_START);
                        // 绑定容器
                        mPlayer.bindContainer(currContainer, true);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 返回 播放器对象
     *
     * @return {@link KsgAssistView}
     */
    public KsgAssistView getPlayer() {
        return mPlayer;
    }

    /**
     * 设置 数据源
     *
     * @param videoUrls 视频地址
     */
    public <T extends VideoEntity> void setVideoUrls(ArrayList<T> videoUrls) {
        this.mVideoUrls.clear();
        this.mVideoUrls.addAll(videoUrls);
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
     * 播放
     *
     * @param position  当前位置
     * @param container 新的容器
     */
    public void onPlay(int position, PlayerContainerView container) {
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
        player.setDataSource(new DataSource(mVideoUrls.get(position).getVideoUrl()));
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
            player.resume();
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

}