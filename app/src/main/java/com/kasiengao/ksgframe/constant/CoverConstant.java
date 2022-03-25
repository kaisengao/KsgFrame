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

        String KEY_ERROR = "error";
    }


    interface ValueKey {

        /**
         * 横竖屏切换
         */
        String KEY_HL_SCREEN_TOGGLE = "hlScreenToggle";

        /**
         * 全屏切换
         */
        String KEY_FULLSCREEN_TOGGLE = "fullscreenToggle";

        /**
         * Controller 状态
         */
        String KEY_CONTROLLER_STATUS = "controllerStatus";

        /**
         * Controller 播放状态
         */
        String KEY_CONTROLLER_PLAY_STATUS = "controllerPlayStatus";
    }

    interface PrivateEvent {

        int CODE_GESTURE_SLIDE_SEEK = -200;
    }

    interface CoverEvent extends ICoverEvent {

        /**
         * 请求 返回
         */
        int CODE_REQUEST_BACK = -100;

        /**
         * 请求 横竖屏切换
         */
        int CODE_REQUEST_HL_SCREEN_TOGGLE = -120;

        /**
         * 请求 全屏切换
         */
        int CODE_REQUEST_FULLSCREEN_TOGGLE = -140;
    }

}
