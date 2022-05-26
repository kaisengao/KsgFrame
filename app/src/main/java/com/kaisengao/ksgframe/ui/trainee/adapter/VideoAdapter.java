package com.kaisengao.ksgframe.ui.trainee.adapter;

import androidx.appcompat.widget.AppCompatRatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kaisengao.base.util.GlideUtil;
import com.kaisengao.ksgframe.ui.trainee.bean.VideoBean;
import com.kaisengao.ksgframe.R;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: VideoAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/28 0:37
 * @Description: 预告视频Adapter
 */
public class VideoAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {

    public VideoAdapter() {
        super(R.layout.item_video);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, VideoBean item) {
        holder.setText(R.id.item_video_title,item.getMovieName());
        holder.setText(R.id.item_video_summary,item.getType().toString());
        holder.<AppCompatRatingBar>getView(R.id.item_video_rating).setRating((float) (1 + Math.random() * (5 - 1 + 1)));

        GlideUtil.loadImageRound(getContext(), item.getCoverImg(), holder.getView(R.id.item_cover_image),4);
    }
}
