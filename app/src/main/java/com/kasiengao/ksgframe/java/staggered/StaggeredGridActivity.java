package com.kasiengao.ksgframe.java.staggered;

import android.view.Window;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kasiengao.base.configure.ThreadPool;
import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;

/**
 * @ClassName: StaggeredGirdActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:19
 * @Description: 瀑布流
 */
public class StaggeredGridActivity extends BaseToolbarActivity {

    private int mCount = 0;

    private RecyclerView mRecyclerView;

    private StaggeredGridAdapter mAdapter;

    private StaggeredGridModel mModel;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_staggered_grid;
    }

    @Override
    protected void initWindow() {
        super.initWindow();
        // 打开FEATURE_CONTENT_TRANSITIONS开关(可选)，这个开关默认是打开的
        super.requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // recycler
        this.mRecyclerView = findViewById(R.id.java_staggered_gird);
        // Init Adapter
        this.initAdapter();
    }

    @Override
    protected void initData() {
        super.initData();
        // Model
        this.mModel = new StaggeredGridModel();
        // Data
        this.mAdapter.setList(mModel.requestGridBeans());
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Adapter
        this.mAdapter = new StaggeredGridAdapter(this);
        this.mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            ThreadPool.MainThreadHandler.getInstance().post(() -> {
                // 计数
                this.mCount++;
                // 添加更多
                this.mAdapter.addData(this.mModel.requestGridBeans());
                // 结束加载
                if (mCount >= 2) {
                    this.mAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    this.mAdapter.getLoadMoreModule().loadMoreComplete();
                }
            }, 3000);
        });
        // Recycler
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 防止item位置交换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        this.mRecyclerView.setLayoutManager(layoutManager);
        this.mRecyclerView.addItemDecoration(new StaggeredItemDecoration(20));
        this.mRecyclerView.setAdapter(mAdapter);
    }
}
