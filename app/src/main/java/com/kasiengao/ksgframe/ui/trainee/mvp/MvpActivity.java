package com.kasiengao.ksgframe.ui.trainee.mvp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BasePresenterActivity;

/**
 * @ClassName: MvpActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/27 23:55
 * @Description: Mvp
 */
public class MvpActivity extends BasePresenterActivity<MvpContract.IPresenter> implements MvpContract.IView{

//    private SwipeRefreshLayout mMvpRefresh;

    private TrailerAdapter mTrailerAdapter;

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
        this.setTitle(R.string.mvp_title);
        // Init Adapter
        this.initAdapter();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Init
        // Adapter
        this.mTrailerAdapter = new TrailerAdapter(this);
        // Recycler
        RecyclerView mvpRecycler = findViewById(R.id.mvp_recycler);
        mvpRecycler.setLayoutManager(new LinearLayoutManager(this));
        mvpRecycler.setAdapter(this.mTrailerAdapter);
        // Refresh
//        this.mMvpRefresh = findViewById(R.id.mvp_refresh);
//        this.mMvpRefresh.setOnRefreshListener(this);
//        this.mMvpRefresh.setRefreshing(true);
//        this.onRefresh();
    }

//    /**
//     * 下拉刷新
//     */
//    @Override
//    public void onRefresh() {
//        // 请求 预告视频列表
//        this.mPresenter.requestTrailerList();
//    }

    /**
     * 返回 预告视频列表
     *
     * @param trailerBean 预告视频列表
     */
    @Override
    public void resultTrailerList(TrailerBean trailerBean) {
        // 赋值数据
        this.mTrailerAdapter.setTrailerBeans(trailerBean.getTrailers());
//        // 关闭刷新动画
//        this.mMvpRefresh.setRefreshing(false);
    }
}
