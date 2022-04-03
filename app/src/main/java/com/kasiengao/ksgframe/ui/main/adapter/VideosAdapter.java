package com.kasiengao.ksgframe.ui.main.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.button.MaterialButton;
import com.kaisengao.base.util.GlideUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.ui.main.bean.VideoBean;
import com.kasiengao.ksgframe.ui.main.player.ListPlayer;
import com.ksg.ksgplayer.player.IPlayer;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: VideosAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 10:23
 * @Description:
 */
public class VideosAdapter extends BaseQuickAdapter<VideoBean, VideosAdapter.ViewHolder> {

    public VideosAdapter() {
        super(R.layout.item_main_video);
    }

    @Override
    protected void convert(@NotNull ViewHolder holder, VideoBean item) {
        // 头像
        GlideUtil.loadImage(getContext(), item.getAvatar(), holder.getView(R.id.item_video_avatar));
        // 昵称
        holder.setText(R.id.item_video_nickname, item.getNickname());
        // 简介
        holder.setText(R.id.item_video_introduce, item.getIntroduce());
        // 标题
        holder.setText(R.id.item_video_title, item.getMovieName());
        // 赞
        holder.mPraiseView.setText(item.getPraise());
        holder.mPraiseView.setSelected(item.isPraised());
        // 踩
        holder.getView(R.id.item_video_interact_stepped).setSelected(item.isStepped());
        // 评论
        holder.setText(R.id.item_video_interact_comment, item.getComment());
        // 分享
        holder.setText(R.id.item_video_interact_share, item.getShare());
        // 设置封面图
        holder.mPlayContainer.setCoverImage(item.getCoverImg());
    }

    protected class ViewHolder extends BaseViewHolder {

        private final MaterialButton mPraiseView;

        private final MaterialButton mSteppedView;

        private final PlayerContainerView mPlayContainer;

        public ViewHolder(@NotNull View view) {
            super(view);
            this.mPraiseView = getView(R.id.item_video_interact_praise);
            this.mSteppedView = getView(R.id.item_video_interact_stepped);
            // 视频容器 设置比例16/9
            this.mPlayContainer = getView(R.id.item_player_container);
            ViewGroup.LayoutParams layoutParams = mPlayContainer.getLayoutParams();
            layoutParams.height = getRecyclerView().getWidth() * 9 / 16;
            this.mPlayContainer.requestLayout();
            // 初始状态位
            this.mPlayContainer.setPlayerState(IPlayer.STATE_IDLE);
            // 赞
            this.getView(R.id.item_video_interact_praise).setOnClickListener(v -> {
                VideoBean videoBean = getData().get(getLayoutPosition());
                videoBean.setPraised(!videoBean.isPraised());
                if (videoBean.isPraised()) {
                    videoBean.addPraise();
                } else {
                    videoBean.subPraise();
                }
                this.mPraiseView.setText(videoBean.getPraise());
                this.mPraiseView.setSelected(videoBean.isPraised());
            });
            // 踩
            this.getView(R.id.item_video_interact_stepped).setOnClickListener(v -> {
                VideoBean videoBean = getData().get(getLayoutPosition());
                videoBean.setStepped(!videoBean.isStepped());
                this.mSteppedView.setSelected(videoBean.isStepped());
            });
            // 评论
            this.getView(R.id.item_video_interact_comment).setOnClickListener(v -> {
                int layoutPosition = getLayoutPosition();
                // 进入详情页
                ListPlayer.getInstance().onEnterDetail(layoutPosition, getData().get(layoutPosition), mPlayContainer);
            });
            // 分享
            this.getView(R.id.item_video_interact_share).setOnClickListener(v -> {
                // 回调上级
                setOnItemChildClick(v, getLayoutPosition());
            });
            // Play
            this.mPlayContainer.setOnClickListener(v -> {
                int layoutPosition = getLayoutPosition();
                // 播放视频
                ListPlayer.getInstance().onPlay(layoutPosition, getData().get(layoutPosition), mPlayContainer);
            });
            // ItemClick
            view.setOnClickListener(v -> {
                int layoutPosition = getLayoutPosition();
                // 进入详情页
                ListPlayer.getInstance().onEnterDetail(layoutPosition, getData().get(layoutPosition), mPlayContainer);
            });
        }
    }
}