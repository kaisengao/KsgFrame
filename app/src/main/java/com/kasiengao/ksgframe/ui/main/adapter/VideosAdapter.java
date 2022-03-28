package com.kasiengao.ksgframe.ui.main.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.button.MaterialButton;
import com.kaisengao.base.util.GlideUtil;
import com.kaisengao.base.util.ToastUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.player.ListPlayer;
import com.kasiengao.ksgframe.ui.main.bean.VideoBean;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: VideosAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 10:23
 * @Description:
 */
public class VideosAdapter extends BaseQuickAdapter<VideoBean, VideosAdapter.ViewHolder> {

    private int mParentWidth;

    public VideosAdapter() {
        super(R.layout.item_main_video);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        this.mParentWidth = parent.getWidth();
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(@NotNull ViewHolder holder, VideoBean item) {
        // 头像
        GlideUtil.loadImage(getContext(), item.getAvatar(), holder.getView(R.id.item_video_avatar));
        // 昵称
        holder.setText(R.id.item_video_nickname, item.getNickname());
        // 简介
        holder.setText(R.id.item_video_profile, item.getProfile());
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

    class ViewHolder extends BaseViewHolder {

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
            layoutParams.height = mParentWidth * 9 / 16;
            this.mPlayContainer.requestLayout();
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
                ToastUtil.showShort("假的！假的！假的！假的！");
            });
            // 分享
            this.getView(R.id.item_video_interact_share).setOnClickListener(v -> {
                ToastUtil.showShort("假的！假的！假的！假的！");
            });
            // Play
            this.mPlayContainer.setOnClickListener(v -> {
                int layoutPosition = getLayoutPosition();
                ListPlayer.getInstance().onPlay(layoutPosition, mPlayContainer);
            });
        }
    }
}