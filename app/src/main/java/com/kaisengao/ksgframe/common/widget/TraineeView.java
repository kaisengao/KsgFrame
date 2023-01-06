package com.kaisengao.ksgframe.common.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kaisengao.ksgframe.ui.trainee.gesture.GestureActivity;
import com.kaisengao.ksgframe.ui.trainee.loadpage.LoadPageActivity;
import com.kaisengao.ksgframe.ui.trainee.mvp.MvpActivity;
import com.kaisengao.ksgframe.ui.trainee.pip.PipActivity;
import com.kaisengao.ksgframe.ui.trainee.player.PlayerActivity;
import com.kaisengao.ksgframe.ui.trainee.retrofit.RxRetrofitActivity;
import com.kaisengao.ksgframe.ui.trainee.staggered.StaggeredGridActivity;
import com.kaisengao.ksgframe.R;
import com.kaisengao.ksgframe.ui.trainee.grid.TouchGridActivity;
import com.kaisengao.ksgframe.ui.trainee.mvvm.MvvmActivity;

/**
 * @ClassName: TraineeView
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/2 15:49
 * @Description: 长达好几年的练习生页面
 */
public class TraineeView extends LinearLayout {

    public TraineeView(@NonNull Context context) {
        this(context, null);
    }

    public TraineeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        // ContentView
        View.inflate(getContext(), R.layout.layout_trainee, this);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setOnClickListener(this::onClick);
        // Mvp
        this.findViewById(R.id.trainee_mvp).setOnClickListener(this::onClick);
        // MvVM
        this.findViewById(R.id.trainee_mvvm).setOnClickListener(this::onClick);
        // Rx+Retrofit
        this.findViewById(R.id.trainee_rx_retrofit).setOnClickListener(this::onClick);
        // Staggered
        this.findViewById(R.id.trainee_staggered).setOnClickListener(this::onClick);
        // Player
        this.findViewById(R.id.trainee_player).setOnClickListener(this::onClick);
        // TouchGrid
        this.findViewById(R.id.trainee_touch_grid).setOnClickListener(this::onClick);
        // GestureView
        this.findViewById(R.id.trainee_gesture).setOnClickListener(this::onClick);
        // Loadpage
        this.findViewById(R.id.trainee_loadpage).setOnClickListener(this::onClick);
        // Pip
        this.findViewById(R.id.trainee_pip).setOnClickListener(this::onClick);
    }

    /**
     * onClick
     *
     * @param v View
     */
    private void onClick(View v) {
        int id = v.getId();
        if (id == R.id.trainee_mvp) {
            this.getContext().startActivity(new Intent(getContext(), MvpActivity.class));
        } else if (id == R.id.trainee_mvvm) {
            this.getContext().startActivity(new Intent(getContext(), MvvmActivity.class));
        } else if (id == R.id.trainee_rx_retrofit) {
            this.getContext().startActivity(new Intent(getContext(), RxRetrofitActivity.class));
        } else if (id == R.id.trainee_staggered) {
            this.getContext().startActivity(new Intent(getContext(), StaggeredGridActivity.class));
        } else if (id == R.id.trainee_player) {
            PlayerActivity.startAc(getContext(), "");
        } else if (id == R.id.trainee_touch_grid) {
            this.getContext().startActivity(new Intent(getContext(), TouchGridActivity.class));
        } else if (id == R.id.trainee_gesture) {
            this.getContext().startActivity(new Intent(getContext(), GestureActivity.class));
        } else if (id == R.id.trainee_loadpage) {
            this.getContext().startActivity(new Intent(getContext(), LoadPageActivity.class));
        } else if (id == R.id.trainee_pip) {
            this.getContext().startActivity(new Intent(getContext(), PipActivity.class));
        }
    }
}