package com.ksg.ksgplayer.listener;

/**
 * @author kaisengao
 * @create: 2019/3/7 13:55
 * @describe: 手势 触摸事件回调
 */
public interface OnTouchGestureListener {

    /**
     * 亮度手势，手指在Layout左半部上下滑动时候调用
     *
     * @param percent 百分比
     */
    void onBrightnessGesture(float percent);

    /**
     * 音量手势，手指在Layout右半部上下滑动时候调用
     *
     * @param percent 百分比
     */
    void onVolumeGesture(float percent);

    /**
     * 快进快退手势，手指在Layout左右滑动的时候调用
     *
     * @param percent 百分比
     */
    void onSlidingGesture(float percent);

    /**
     * 单击手势，确认是单击的时候调用
     */
    void onSingleTapGesture();

    /**
     * 双击手势，确认是双击的时候调用
     */
    void onDoubleTapGesture();

    /**
     * 按下手势，第一根手指按下时候调用
     */
    void onDown();

    /**
     * 快进后退手势 滑动结束
     */
    void onSlidingEndGesture();

    /**
     * 滑动结束
     */
    void onEndGesture();
}
