package com.kaisengao.ksgframe.ui.trainee.mvp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kaisengao.base.configure.ThreadPool;
import com.kaisengao.ksgframe.ui.trainee.bean.VideoBean;
import com.kaisengao.ksgframe.R;
import com.kaisengao.ksgframe.common.widget.CSwipeRefreshLayout;
import com.kaisengao.ksgframe.ui.trainee.adapter.VideoAdapter;
import com.kasiengao.mvp.java.BasePresenterActivity;

import java.util.List;

/**
 * @ClassName: MvpActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/27 23:55
 * @Description: Mvp
 * TODO (2022年04月02日 建议弃用)
 */
public class MvpActivity extends BasePresenterActivity<MvpContract.IPresenter> implements SwipeRefreshLayout.OnRefreshListener, MvpContract.IView {

    private CSwipeRefreshLayout mRefresh;

    private VideoAdapter mAdapter;

    private List<VideoBean> mVideos;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvp;
    }

    @Override
    public MvpContract.IPresenter initPresenter() {
        return new MvpPresenter();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.trainee_mvp);
        // Init Adapter
        this.initAdapter();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Adapter
        this.mAdapter = new VideoAdapter();
        // Recycler
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter);
        // Refresh
        this.mRefresh = findViewById(R.id.refresh);
        this.mRefresh.setOnRefreshListener(this);
        this.mRefresh.setRefreshing(true);
        this.onRefresh();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        // 请求 预告视频列表
        this.mPresenter.requestVideos();
    }

    /**
     * 返回 预告视频列表
     *
     * @param videos 预告视频列表
     */
    @Override
    public void resultVideos(List<VideoBean> videos) {
        this.mVideos = videos;
        // 模拟假数据 延迟3秒
        ThreadPool.MainThreadHandler.getInstance().post(mDataRunnable, 3000);
    }

    private final Runnable mDataRunnable = () -> {
        // 赋值数据
        this.mAdapter.setList(mVideos);
        // 关闭刷新动画
        this.mRefresh.setRefreshing(false);
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadPool.MainThreadHandler.getInstance().removeCallbacks(mDataRunnable);
    }
}
