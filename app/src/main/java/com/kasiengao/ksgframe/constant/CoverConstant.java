package com.kasiengao.ksgframe.constant;

import com.ksg.ksgplayer.cover.ICoverEvent;

/**
 * @ClassName: CoverConstant
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 16:49
 * @Description:
 */
public interface CoverConstant {

    interface CoverKey {

        String KEY_GESTURE = "gesture";

        String KEY_LOADING = "loading";

        String KEY_CONTROLLER = "controller";

        String KEY_LAND_CONTROLLER = "landController";

        String KEY_SMALL_CONTROLLER = "smallController";

        String KEY_PPX_CONTROLLER = "PPXController";

        String KEY_UPLOADER = "uploader";

        String KEY_DANMAKU = "danmaku";

    }

    interface ValueKey {

        /**
         * 手势 单击
         */
        String KEY_GESTURE_SINGLE_TAP = "signalTab";

        /**
         * 手势 双击
         */
        String KEY_GESTURE_DOUBLE_TAP = "doubleTab";

        /**
         * 手势 长按
         */
        String KEY_GESTURE_LONG_PRESS = "longPress";

        /**
         * 手势 长按结束
         */
        String KEY_GESTURE_LONG_PRESS_END = "longPressEnd";

        /**
         * 手势 PaddingTop
         */
        String KEY_GESTURE_PADDING_TOP = "gesturePaddingTop";

        /**
         * 控制器 隐藏
         */
        String KEY_HIDE_CONTROLLER = "hideController";

        /**
         * 横竖屏切换
         */
        String KEY_LP_SCREEN_TOGGLE = "lpScreenToggle";

        /**
         * 全屏切换
         */
        String KEY_FULLSCREEN_TOGGLE = "fullscreenToggle";

        /**
         * UP主信息
         */
        String KEY_UPLOADER_DATA = "uploaderData";
    }

    interface PrivateEvent {

        /**
         * 弹幕开
         */
        int CODE_REQUEST_DANMAKU_OPEN = -100;

        /**
         * 弹幕关
         */
        int CODE_REQUEST_DANMAKU_CLOSE = -101;

        /**
         * 手势 开启/关闭
         */
        int CODE_GESTURE_ENABLED = 150;
    }

    interface CoverEvent extends ICoverEvent {

        /**
         * 请求 返回
         */
        int CODE_REQUEST_BACK = -100;

        /**
         * 请求 横竖屏切换
         */
        int CODE_REQUEST_LP_SCREEN_TOGGLE = -120;

        /**
         * 请求 全屏切换
         */
        int CODE_REQUEST_FULLSCREEN_TOGGLE = -140;

        /**
         * 请求 进入全屏
         */
        int CODE_REQUEST_FULLSCREEN_ENTER = -141;

        /**
         * 请求 退出全屏
         */
        int CODE_REQUEST_FULLSCREEN_EXIT = -142;

        /**
         * 请求 进度自动更新状态
         */
        int CODE_REQUEST_TIMER_UPDATE_STATE = -150;
    }
}
