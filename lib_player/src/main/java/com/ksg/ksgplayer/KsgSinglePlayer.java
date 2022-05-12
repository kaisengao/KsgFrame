package com.ksg.ksgplayer;

import android.graphics.Color;
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

    private static KsgSinglePlayer instance;

    private boolean isOverlap;

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
        // 创建遍历缓存池
        this.mVariableCaches = new HashMap<>();
        // Init Player
        this.initPlayer();
    }

    /**
     * Init Player
     */
    private void initPlayer() {
        this.mPlayer = new KsgAssistView(AppFactory.application());
        this.mPlayer.setBackgroundColor(Color.BLACK);
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
    public <T> void putVariable(int key, T value) {
        this.mVariableCaches.put(key, value);
    }

    /**
     * 获取 变量
     */
    public <T> T getVariable(int key) {
        return getVariable(key, null);
    }

    /**
     * 获取 变量
     *
     * @param defValue 默认值
     */
    @SuppressWarnings("unchecked")
    public <T> T getVariable(int key, T defValue) {
        Object value = mVariableCaches.get(key);
        return value == null ? defValue : (T) value;
    }

    /**
     * 删除 变量
     */
    public void removeVariable(int key) {
        this.mVariableCaches.remove(key);
    }

    /**
     * 绑定 视图容器
     *
     * @param container      container
     * @param updateRenderer 更新渲染器
     */
    public void bindContainer(ViewGroup container, boolean updateRenderer) {
        this.mPlayer.bindContainer(container, updateRenderer);
    }

    /**
     * 解绑 视图容器
     */
    public void unbindContainer() {
        this.mPlayer.unbindContainer();
    }

    /**
     * 播放
     */
    public void onStart(DataSource dataSource) {
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
        if (isOverlap) {
            return;
        }
        this.getPlayer().resume();
    }

    /**
     * 暂停
     */
    public void onPause() {
        if (!mPlayer.isItPlaying()) {
            return;
        }
        this.getPlayer().pause();
    }

    /**
     * 覆盖状态
     *
     * @param overlap overlap
     */
    public void setOverlap(boolean overlap) {
        this.isOverlap = overlap;
        if (getPlayer().isItPlaying()) {
            if (isOverlap) {
                this.onPause();
            } else {
                this.onResume();
            }
        } else {
            if (getPlayer().getState() == IPlayer.STATE_PREPARED) {
                this.getPlayer().start();
            }
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