package com.kasiengao.ksgframe.java.grid;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kaisengao.base.util.ToastUtil;
import com.kasiengao.ksgframe.R;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: TouchGridAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2021/6/9 10:38
 * @Description: 拖动Grid列表
 */
public class TouchGridAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TouchGridAdapter() {
        super(R.layout.item_touch_grid);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String item) {
        holder.setText(R.id.item_grid, item);
        holder.getView(R.id.item_grid).setOnClickListener(v -> {
            // Toast
            ToastUtil.showShort("item = " + item);
        });
    }
}