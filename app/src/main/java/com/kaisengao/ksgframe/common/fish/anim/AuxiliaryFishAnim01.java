package com.kaisengao.ksgframe.common.fish.anim;

import android.content.Context;

import com.kaisengao.ksgframe.common.fish.factory.FishFactory;
import com.kaisengao.ksgframe.common.fish.anim.base.BaseAuxiliaryFishAnim;


/**
 * @ClassName: AuxiliaryFishAnim01
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/7 10:30
 * @Description: 小丑鱼
 */
public class AuxiliaryFishAnim01 extends BaseAuxiliaryFishAnim {

    public AuxiliaryFishAnim01(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        // 设置帧动画间隔
        this.setFrameDuration(75);
        // 解码图片并缓存
        this.decodeBitmap(FishFactory.getInstance().getFishAuxiliary01Caches(mContext));
    }
}