package com.kaisengao.ksgframe.common.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.kaisengao.ksgframe.R;


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

    /**
     * 全屏动画
     *
     * @param view                    View
     * @param fullScreen              是否全屏
     * @param from                    从哪里开始
     * @param to                      到哪里结束
     * @param animatorUpdateListener  动画每一帧监听
     * @param animatorListenerAdapter 动画事件监听
     */
    public static void fullScreenAnim(View view, boolean fullScreen, int from, int to,
                                      final ValueAnimator.AnimatorUpdateListener animatorUpdateListener,
                                      final AnimatorListenerAdapter animatorListenerAdapter) {
        // 验证一下如果是全屏状态下 且 是横竖屏切换 则不执行动画
        if (fullScreen && view.getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
            return;
        }
        // 属性动画
        ValueAnimator animator = ValueAnimator.ofInt(fullScreen ? from : to, fullScreen ? to : from);
        // 设置动画时长
        animator.setDuration(5000);
        // 回调监听
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束 如果是全屏 设置 match_parent
                if (fullScreen) {
                    view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    view.requestLayout();
                }
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationEnd(animation);
                }
            }
        });
        // 开启动画
        animator.start();
    }
}
