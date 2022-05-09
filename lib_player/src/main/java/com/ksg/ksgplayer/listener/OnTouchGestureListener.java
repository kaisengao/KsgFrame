package com.ksg.ksgplayer.listener;

/**
 * @author kaisengao
 * @create: 2019/3/7 13:55
 * @describe: 手势 触摸事件回调
 */
public interface OnTouchGestureListener {

    /**
     * Down事件
     */
    void onDown();

    /**
     * 单击
     */
    void onSingleTap();

    /**
     * 双击
     */
    void onDoubleTap();

    /**
     * 长按
     */
    void onLongPress();

    /**
     * 长按结束
     */
    void onLongPressEnd();

    /**
     * 亮度
     *
     * @param percent 百分比
     */
    void onSlideBrightness(int percent);

    /**
     * 音量
     *
     * @param percent 百分比
     */
    void onSlideVolume(int percent);

    /**
     * 快进退
     *
     * @param percent 百分比
     * @param time    时间
     */
    void onSlideSeek(float percent, String time);

    /**
     * 滑动结束
     */
    void onSlideEnd();

    /**
     * 滑动结束 快进退
     */
    void onSlideEndSeek();
}
