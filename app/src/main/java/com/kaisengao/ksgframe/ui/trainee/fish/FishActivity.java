package com.kaisengao.ksgframe.ui.trainee.fish;


import com.kaisengao.base.configure.ThreadPool;
import com.kaisengao.base.util.ToastUtil;
import com.kaisengao.ksgframe.R;
import com.kaisengao.ksgframe.common.fish.anim.AuxiliaryFishAnim01;
import com.kaisengao.ksgframe.common.fish.anim.AuxiliaryFishAnim02;
import com.kaisengao.ksgframe.common.fish.anim.AuxiliaryFishAnim03;
import com.kaisengao.ksgframe.databinding.ActivityFishBinding;
import com.kaisengao.mvvm.base.activity.BaseActivity;

import java.util.Random;

/**
 * @ClassName: FishActivity
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/26 11:19
 * @Description: 摸鱼 （素材来自网络）
 */
public class FishActivity extends BaseActivity<ActivityFishBinding> {

    private int mAuxiliaryCount = 0;

    private final Random mRandom = new Random();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_fish;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        ThreadPool.MainThreadHandler.getInstance().post(mAuxiliaryFish, 500);
        // Toast
        ToastUtil.showLong("稍等一下哦，鱼儿马上出来！");
    }

    private final Runnable mAuxiliaryFish = new Runnable() {
        @Override
        public void run() {
            if (mAuxiliaryCount > 30) {
                return;
            }
            mAuxiliaryCount++;
            switch (mRandom.nextInt(3)) {
                case 1:
                    mBinding.fish.addItem(new AuxiliaryFishAnim01(FishActivity.this));
                    break;
                case 2:
                    mBinding.fish.addItem(new AuxiliaryFishAnim02(FishActivity.this));
                    break;
                default:
                    mBinding.fish.addItem(new AuxiliaryFishAnim03(FishActivity.this));
                    break;
            }
            ThreadPool.MainThreadHandler.getInstance().post(this, 2000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mBinding.fish.release();
        ThreadPool.MainThreadHandler.getInstance().removeCallbacks(mAuxiliaryFish);
    }
}