package com.kasiengao.ksgframe.ui.main.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kaisengao.base.util.GlideUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.ui.main.bean.VideoBean;

/**
 * @ClassName: PPXAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/27 21:28
 * @Description: 皮皮虾
 */
public class PPXAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {

    public PPXAdapter() {
        super(R.layout.item_ppx);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, VideoBean item) {
        GlideUtil.loadImage(getContext(),item.getCoverImg(),holder.getView(R.id.cover_image));
    }
}