package com.kasiengao.ksgframe.java.staggered;

import android.app.SharedElementCallback;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kasiengao.base.configure.ThreadPool;
import com.kasiengao.base.util.FileUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;

import java.util.List;

/**
 * @ClassName: StaggeredGirdActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:19
 * @Description: 瀑布流
 */
public class StaggeredGridActivity extends BaseToolbarActivity {

    private int mCount = 0;

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
        // 解决 Android 7.0以上返回跳转前页面完成过场动画之后SimpleDraweeView不显示图片
        this.setExitSharedElementCallback(new SharedElementCallback() {
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                for (View view : sharedElements) {
                    if (view instanceof SimpleDraweeView) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    protected void initWidget() {
        super.initWidget();
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
        this.mAdapter.getLoadMoreModule().setOnLoadMoreListener(() ->
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
                }, 3000));
        // recycler
        RecyclerView recyclerView = findViewById(R.id.java_staggered_gird);
        // Recycler
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // 防止item位置交换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new StaggeredItemDecoration(20));
        recyclerView.setAdapter(mAdapter);
        // 滑动优化 滑动过程中停止加载图片
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Fresco.getImagePipeline().resume();
                } else {
                    Fresco.getImagePipeline().pause();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Init Title
        Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        long size = Fresco.getImagePipelineFactory().getMainFileCache().getSize();
        this.setTitle(String.format(getString(R.string.staggered_grid_title), FileUtil.getFileSizeDescription(size)));
    }
}
