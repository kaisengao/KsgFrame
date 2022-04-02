package com.kasiengao.ksgframe.ui.trainee.mvvm;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaisengao.base.annotations.ReloadAnnotations;
import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.MvvmBinding;
import com.kasiengao.ksgframe.ui.trainee.adapter.VideoAdapter;

/**
 * @ClassName: MvvmActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/11 13:02
 * @Description: MVVM
 */
public class MvvmActivity extends BaseVmActivity<MvvmBinding, MvvmViewModel> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvvm;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Init Adapter
        this.initAdapter();
    }

    /**
     * Init Adapter
     */
    private void initAdapter() {
        // Adapter
        VideoAdapter adapter = new VideoAdapter();
        // Recycler
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        // Data
        this.mViewModel.getVideos().observe(this, videos -> {
            if (videos != null && !videos.isEmpty()) {
                adapter.setList(videos);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        // 请求 视频列表
        this.mViewModel.requestVideos();
    }

    @ReloadAnnotations
    public void onReload(Object target) {
        // Retry 请求 视频列表
        this.mViewModel.requestVideos();
    }
}
