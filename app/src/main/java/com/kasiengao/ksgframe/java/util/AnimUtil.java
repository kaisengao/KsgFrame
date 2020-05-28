package com.kasiengao.ksgframe.java.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.kasiengao.ksgframe.R;


/**
 * @ClassName: AnimUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/28 12:51
 * @Description: 动画工具类
 */
public class AnimUtil {

    public static Animation getTopInAnim(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_top);
    }

    public static Animation getTopOutAnim(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_exit_from_top);
    }

    public static Animation getBottomInAnim(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_enter_from_bottom);
    }

    public static Animation getBottomOutAnim(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.anim_exit_from_bottom);
    }

}
