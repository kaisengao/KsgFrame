package com.kaisengao.ksgframe.common.float.app

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import com.petterp.floatingx.assist.FxAnimation

/**
 * Fx的动画示例
 * */
class FxAnimationImpl(private val defaultTime: Long = 200L) : FxAnimation() {

    override fun fromAnimator(view: FrameLayout?): Animator {
        view?.let {
            val alphaAnim = ObjectAnimator.ofFloat(it, View.ALPHA, 0f, 1f)
            alphaAnim.duration = defaultTime
            alphaAnim.interpolator = AccelerateInterpolator(2f)
            return alphaAnim
        }
        return AnimatorSet()
    }

    override fun toAnimator(view: FrameLayout?): Animator {
        view?.let {
            val alphaAnim = ObjectAnimator.ofFloat(it, View.ALPHA, 1f, 0f)
            alphaAnim.duration = defaultTime
            alphaAnim.interpolator = AccelerateInterpolator(2f)
            return alphaAnim
        }
        return AnimatorSet()
    }
}
