package com.kaisengao.ksgframe.common.fish.anim;

import android.content.Context;

import com.kaisengao.ksgframe.common.fish.factory.FishFactory;
import com.kaisengao.ksgframe.common.fish.anim.base.BaseAuxiliaryFishAnim;


/**
 * @ClassName: AuxiliaryFishAnim02
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/7 10:31
 * @Description: 小黄鱼
 */
public class AuxiliaryFishAnim02 extends BaseAuxiliaryFishAnim {

    public AuxiliaryFishAnim02(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        // 设置帧动画间隔
        this.setFrameDuration(100);
        // 解码图片并缓存
        this.decodeBitmap(FishFactory.getInstance().getFishAuxiliary02Caches(mContext));
    }
}