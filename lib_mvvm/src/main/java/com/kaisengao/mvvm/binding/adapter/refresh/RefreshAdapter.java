package com.kaisengao.mvvm.binding.adapter.refresh;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @ClassName: RefreshAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/3 14:48
 * @Description:
 */
public class RefreshAdapter {

    @BindingAdapter("setRefreshing")
    public static void setViewHeight(SwipeRefreshLayout refreshLayout, boolean refreshing) {
        refreshLayout.setRefreshing(refreshing);
    }
}