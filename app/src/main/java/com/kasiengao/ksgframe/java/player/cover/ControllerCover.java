package com.kasiengao.ksgframe.java.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.kasiengao.ksgframe.R;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;
import com.ksg.ksgplayer.receiver.BaseCover;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ClassName: ControllerCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 17:10
 * @Description: 控制器 Cover
 */
public class ControllerCover extends BaseCover implements OnTimerUpdateListener {

    private Unbinder mBind;

    @BindView(R.id.cover_controller_progress)
    AppCompatTextView mTextView;

    public ControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_controller, null);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        this.mBind = ButterKnife.bind(this, getView());
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
        this.mBind.unbind();
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    /**
     * @param curr             当前进度
     * @param duration         总进度
     * @param bufferPercentage 缓冲进度
     */
    @Override
    public void onTimerUpdate(long curr, long duration, long bufferPercentage) {
        String progress = curr + "\n" + duration + "\n" + bufferPercentage + "";
        this.mTextView.setText(progress);
    }
}
