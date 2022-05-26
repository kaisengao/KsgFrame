package com.kaisengao.ksgframe.ui.dialog.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kaisengao.ksgframe.R;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: ShareAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/2 17:22
 * @Description: 分享列表
 */
public class ShareAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShareAdapter() {
        super(R.layout.item_share);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String item) {


    }
}