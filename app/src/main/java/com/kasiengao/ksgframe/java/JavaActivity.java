package com.kasiengao.ksgframe.java;

import android.content.Intent;

import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.grid.TouchGridActivity;
import com.kasiengao.ksgframe.java.mvp.MvpActivity;
import com.kasiengao.ksgframe.java.mvvm.MvvmActivity;
import com.kasiengao.ksgframe.java.player.PlayerActivity;
import com.kasiengao.ksgframe.java.retrofit.RxRetrofitActivity;
import com.kasiengao.ksgframe.java.staggered.StaggeredGridActivity;
import com.kasiengao.mvp.java.BaseToolbarActivity;

/**
 * @ClassName: JavaActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 18:43
 * @Description: Java 版本
 */
public class JavaActivity extends BaseToolbarActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_java;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.java_title);
        // Mvp 模式
        this.findViewById(R.id.java_mvp).setOnClickListener(v -> {
            // Mvp 模式
            this.startActivity(new Intent(this, MvpActivity.class));
        });
        // MvVM 模式
        this.findViewById(R.id.java_mvvm).setOnClickListener(v -> {
            // MvVM 模式
            this.startActivity(new Intent(this, MvvmActivity.class));
        });
        // Rx+Retrofit
        this.findViewById(R.id.java_rx_retrofit).setOnClickListener(v -> {
            // Rx+Retrofit
            this.startActivity(new Intent(this, RxRetrofitActivity.class));
        });
        // StaggeredGrid
        this.findViewById(R.id.java_staggered_grid).setOnClickListener(v -> {
            // StaggeredGrid
            this.startActivity(new Intent(this, StaggeredGridActivity.class));
        });
        // KsgVideoPlayer
        this.findViewById(R.id.java_player).setOnClickListener(v -> {
            // KsgVideoPlayer
            this.startActivity(new Intent(this, PlayerActivity.class));
        });
        // TouchGrid
        this.findViewById(R.id.btn_grid).setOnClickListener(v -> {
            // TouchGrid
            this.startActivity(new Intent(this, TouchGridActivity.class));
        });
    }
}
