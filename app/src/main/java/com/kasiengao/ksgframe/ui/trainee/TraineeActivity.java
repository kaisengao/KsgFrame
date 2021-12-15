package com.kasiengao.ksgframe.ui.trainee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.kaisengao.base.loadpage.KsgLoadFrame;
import com.kaisengao.base.loadpage.load.EmptyViewLoad;
import com.kaisengao.base.loadpage.load.ErrorViewLoad;
import com.kaisengao.base.loadpage.load.LoadingViewLoad;
import com.kaisengao.base.loadpage.widget.LoadContainer;
import com.kaisengao.base.util.ToastUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.ui.trainee.gesture.GestureActivity;
import com.kasiengao.ksgframe.ui.trainee.grid.TouchGridActivity;
import com.kasiengao.ksgframe.ui.trainee.mvp.MvpActivity;
import com.kasiengao.ksgframe.ui.trainee.mvvm.MvvmActivity;
import com.kasiengao.ksgframe.ui.trainee.player.PlayerActivity;
import com.kasiengao.ksgframe.ui.trainee.retrofit.RxRetrofitActivity;
import com.kasiengao.ksgframe.ui.trainee.staggered.StaggeredGridActivity;
import com.kasiengao.mvp.java.BaseToolbarActivity;

/**
 * @ClassName: TraineeActivity
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 15:30
 * @Description: 长达好几年的练习生页面
 */
public class TraineeActivity extends BaseToolbarActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TraineeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_trainee;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.bar_trainee);
        // Mvp
        this.findViewById(R.id.trainee_mvp).setOnClickListener(this::onClick);
        // MvVM
        this.findViewById(R.id.trainee_mvvm).setOnClickListener(this::onClick);
        // Rx+Retrofit
        this.findViewById(R.id.trainee_rx_retrofit).setOnClickListener(this::onClick);
        // Staggered
        this.findViewById(R.id.trainee_staggered).setOnClickListener(this::onClick);
        // KsgVideoPlayer
        this.findViewById(R.id.trainee_player).setOnClickListener(this::onClick);
        // TouchGrid
        this.findViewById(R.id.trainee_touch_grid).setOnClickListener(this::onClick);
        // GestureView
        this.findViewById(R.id.trainee_gesture).setOnClickListener(this::onClick);

        LoadContainer container = KsgLoadFrame.getInstance().bindLoadContainer(this, view -> {
            ToastUtil.showShort("响应点击事件咯！");
        });

        container.showLoad(EmptyViewLoad.class);
        container.showLoad(ErrorViewLoad.class);
        container.showLoad(LoadingViewLoad.class);
    }

    /**
     * onClick
     *
     * @param v View
     */
    private void onClick(View v) {
        int id = v.getId();
        if (id == R.id.trainee_mvp) {
            this.startActivity(new Intent(this, MvpActivity.class));
        } else if (id == R.id.trainee_mvvm) {
            this.startActivity(new Intent(this, MvvmActivity.class));
        } else if (id == R.id.trainee_rx_retrofit) {
            this.startActivity(new Intent(this, RxRetrofitActivity.class));
        } else if (id == R.id.trainee_staggered) {
            this.startActivity(new Intent(this, StaggeredGridActivity.class));
        } else if (id == R.id.trainee_player) {
            this.startActivity(new Intent(this, PlayerActivity.class));
        } else if (id == R.id.trainee_touch_grid) {
            this.startActivity(new Intent(this, TouchGridActivity.class));
        } else if (id == R.id.trainee_gesture) {
            this.startActivity(new Intent(this, GestureActivity.class));
        }
    }
}
