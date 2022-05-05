package com.ksg.ksgplayer;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.kaisengao.base.factory.AppFactory;
import com.ksg.ksgplayer.cover.CoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.widget.KsgAssistView;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: KsgSinglePlayer
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/5 10:50
 * @Description: 单例 主要用于一些无缝播放的与列表中
 */
public class KsgSinglePlayer {

    private static final int CURR_POSITION = 0x80;

    private static final int CURR_CONTAINER = 0x90;

    private static KsgSinglePlayer instance;

    private boolean isOverlap;

    private boolean mHideContainer;

    private Map<Integer, Object> mVariableCaches;

    private KsgAssistView mPlayer;

    private CoverManager mCoverManager;

    public static KsgSinglePlayer getInstance() {
        if (instance == null) {
            synchronized (KsgSinglePlayer.class) {
                if (instance == null) {
                    instance = new KsgSinglePlayer();
                }
            }
        }
        return instance;
    }

    private KsgSinglePlayer() {
        this.mVariableCaches = new HashMap<>();
        // Init Player
        this.initPlayer();
    }

    /**
     * Init Player
     */
    private void initPlayer() {
        this.mPlayer = new KsgAssistView(AppFactory.application());
        this.mPlayer.setBackgroundColor(R.color.black);
        // 创建 Cover管理器
        this.mCoverManager = new CoverManager();
        // 设置 Cover管理器
        this.mPlayer.setCoverManager(mCoverManager);
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
     * 缓存 变量
     */
    public void putVariable(int key, Object value) {
        this.mVariableCaches.put(key, value);
    }

    /**
     * 获取 变量
     */
    public Object getVariable(int key) {
        return getVariable(key, null);
    }

    /**
     * 获取 变量
     *
     * @param defValue 默认值
     */
    public Object getVariable(int key, Object defValue) {
        Object value = mVariableCaches.get(key);
        return value == null ? defValue : value;
    }

    /**
     * 记录 坐标
     *
     * @param position 坐标
     */
    public void setPosition(int position) {
        this.putVariable(CURR_POSITION, position);
    }

    /**
     * 返回 坐标
     *
     * @return 坐标
     */
    public int getPosition() {
        return (int) getVariable(CURR_POSITION, -1);
    }

    /**
     * 绑定 视图容器
     *
     * @param container      container
     * @param updateRenderer 更新渲染器
     */
    public void bindContainer(ViewGroup container, boolean updateRenderer) {
        this.putVariable(CURR_CONTAINER, container);
        this.mPlayer.bindContainer(container, updateRenderer);
    }

    /**
     * 解绑 视图容器
     */
    public void unbindContainer() {
        this.mPlayer.unbindContainer();
    }

    /**
     * 返回 视图容器
     *
     * @return container
     */
    public ViewGroup getCurrContainer() {
        return (ViewGroup) getVariable(CURR_CONTAINER);
    }

    /**
     * 播放
     */
    public void onPlay(DataSource dataSource) {
        this.mPlayer.setDataSource(dataSource);
        if (!isOverlap) {
            this.mPlayer.start();
        }
    }

    /**
     * 设置循环播放
     */
    public void setLooping(boolean looping) {
        this.mPlayer.setLooping(looping);
    }

    /**
     * 继续
     */
    public void onResume() {
        if (!mPlayer.isItPlaying()) {
            return;
        }
        // 绑定 视图容器
        ViewGroup currContainer = getCurrContainer();
        if (mHideContainer && currContainer != null) {
            this.getPlayer().bindContainer(currContainer);
        }
        // 继续播放
        this.getPlayer().resume();
        // 恢复状态位
        this.mHideContainer = false;
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
        if (!mPlayer.isItPlaying()) {
            return;
        }
        this.mHideContainer = hideContainer;
        // 解绑 视图容器
        if (mHideContainer) {
            this.unbindContainer();
        }
        // 暂停播放
        this.getPlayer().pause();
    }

    /**
     * 覆盖状态
     *
     * @param overlap overlap
     */
    public void setOverlap(boolean overlap) {
        this.isOverlap = overlap;
        int state = getPlayer().getState();
        if (state == IPlayer.STATE_PAUSE) {
            this.onResume();
        } else if (state == IPlayer.STATE_START) {
            this.onPause(false);
        } else if (state == IPlayer.STATE_PREPARED) {
            this.getPlayer().start();
        }
    }

    /**
     * 返回 覆盖状态
     */
    public boolean isOverlap() {
        return isOverlap;
    }

    /**
     * 释放
     */
    public void release() {
        if (mPlayer != null) {
            this.mPlayer.destroy();
            this.mPlayer = null;
        }
        if (mVariableCaches != null) {
            this.mVariableCaches.clear();
            this.mVariableCaches = null;
        }
        instance = null;
    }
}