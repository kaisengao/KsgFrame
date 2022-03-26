package com.kasiengao.ksgframe.ui.main.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kaisengao.base.util.GlideUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.ui.main.bean.VideoBean;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: VideosAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 10:23
 * @Description:
 */
public class VideosAdapter extends BaseQuickAdapter<VideoBean.TrailersBean, VideosAdapter.ViewHolder> {

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
    protected void convert(@NotNull ViewHolder holder, VideoBean.TrailersBean item) {
        // 标题
        holder.setText(R.id.item_video_title, item.getMovieName());
        // 封面图
        GlideUtil.loadImage(getContext(), item.getCoverImg(), holder.getView(R.id.item_video_cover_image));
    }

    class ViewHolder extends BaseViewHolder {

        public ViewHolder(@NotNull View view) {
            super(view);
            // 设置比例16/9
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = mParentWidth * 9 / 16;
            view.requestLayout();
        }
    }
}