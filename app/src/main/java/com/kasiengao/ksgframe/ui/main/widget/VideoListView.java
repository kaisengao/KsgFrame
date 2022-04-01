package com.kasiengao.ksgframe.ui.main.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.ui.main.adapter.VideosAdapter;
import com.kasiengao.ksgframe.ui.main.bean.VideoBean;
import com.kasiengao.ksgframe.ui.main.player.ListPlayer;
import com.ksg.ksgplayer.player.IPlayer;

import java.util.List;

/**
 * @ClassName: VideoListView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/27 17:22
 * @Description: 支持滚动播放视频
 */
public class VideoListView extends RecyclerView {

    private VideosAdapter mAdapter;

    public VideoListView(@NonNull Context context) {
        super(context);
    }

    public VideoListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        // Init Adapter
        this.initAdapter();
        // 注册滚动事件
        this.addOnScrollListener(mScrollListener);
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Adapter
        this.mAdapter = new VideosAdapter();
        // Recycler
        this.setLayoutManager(new LinearLayoutManager(getContext()));
        this.setAdapter(mAdapter);
    }

    /**
     * 设置 数据源
     */
    public void setData(List<VideoBean> videos) {
        // Adapter
        this.mAdapter.setList(videos);
        // 自动播放
        this.smoothScrollBy(0, 1);
    }

    /**
     * 滚动事件
     */
    private final RecyclerView.OnScrollListener mScrollListener = new OnScrollListener() {
        private int visibleCount = 0;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // 验证是否可操作
            if (!ListPlayer.getInstance().isOperable()) {
                return;
            }
            // 滚动状态
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 停止滚动
                this.onScrollIdle();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 验证是否可操作
            if (!ListPlayer.getInstance().isOperable()) {
                return;
            }
            // 计算当前正在播放的组件是否已经滚出了屏幕
            PlayerContainerView currContainer = ListPlayer.getInstance().getCurrContainer();
            if (currContainer != null && !currContainer.getLocalVisibleRect(new Rect())) {
                // 暂停播放
                ListPlayer.getInstance().onPause();
            }
        }

        /**
         * 停止滚动
         */
        private void onScrollIdle() {
            // 计算当前可视Item数量
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                int firstVisibleItem = manager.findFirstVisibleItemPosition();
                int lastVisibleItem = manager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;
            }
            // 计算当前可播放的Item
            for (int i = 0; i < visibleCount; i++) {
                View itemView = getChildAt(i);
                if (itemView != null) {
                    PlayerContainerView playContainer = itemView.findViewById(R.id.item_player_container);
                    if (playContainer != null) {
                        // 获取PlayContainer的可视区域
                        Rect rect = new Rect();
                        playContainer.getLocalVisibleRect(rect);
                        // 验证 PlayContainer是否完全可视 (Rect.top 恒为 0)
                        if (rect.top != 0) {
                            continue;
                        }
                        ListPlayer listPlayer = ListPlayer.getInstance();
                        // 获取当前Item的Position
                        int layoutPosition = getChildLayoutPosition(itemView);
                        // 验证当前可视的Item是否是正在播放的Item
                        PlayerContainerView currContainer = listPlayer.getCurrContainer();
                        if (layoutPosition == listPlayer.getCurrPosition()
                                && currContainer != null
                                && currContainer == playContainer) {
                            // 判断状态 如果是暂停状态则继续播放
                            if (currContainer.getPlayerState() == IPlayer.STATE_PAUSE) {
                                // 继续播放
                                listPlayer.onResume();
                                // Break
                                break;
                            }
                            // 判断状态 如果是正在播放则跳出方法
                            if (currContainer.getPlayerState() == IPlayer.STATE_START) {
                                // Break
                                break;
                            }
                        }
                        // 播放视频
                        listPlayer.onPlay(layoutPosition, mAdapter.getData().get(layoutPosition), playContainer);
                        // Break
                        break;
                    }
                }
            }
        }
    };


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 解绑滚动事件
        this.removeOnScrollListener(mScrollListener);
    }
}