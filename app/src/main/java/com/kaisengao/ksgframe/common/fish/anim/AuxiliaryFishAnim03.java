package com.kaisengao.ksgframe.common.fish.anim;

import android.content.Context;

import com.kaisengao.ksgframe.common.fish.factory.FishFactory;
import com.kaisengao.ksgframe.common.fish.anim.base.BaseAuxiliaryFishAnim;


/**
 * @ClassName: AuxiliaryFishAnim03
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/7 10:32
 * @Description: 大蓝鱼
 */
public class AuxiliaryFishAnim03 extends BaseAuxiliaryFishAnim {

    public AuxiliaryFishAnim03(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();
        // 设置帧动画间隔
        this.setFrameDuration(70);
        // 解码图片并缓存
        this.decodeBitmap(FishFactory.getInstance().getFishAuxiliary03Caches(mContext));
    }
}