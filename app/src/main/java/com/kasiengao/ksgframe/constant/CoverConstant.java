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

        String KEY_UPLOADER = "uploader";
    }

    interface ValueKey {

        /**
         * UP主信息
         */
        String KEY_UPLOADER_DATA = "uploaderData";

        /**
         * 控制器 显示/隐藏
         */
        String KEY_SWITCH_CONTROLLER = "switchController";

        /**
         * 控制器 隐藏
         */
        String KEY_HIDE_CONTROLLER = "hideController";

        /**
         * 播放状态 播放/暂停
         */
        String KEY_SWITCH_PLAY = "switchPlay";

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
         * 请求 进入全屏
         */
        int CODE_REQUEST_FULLSCREEN_ENTER = -140;

        /**
         * 请求 退出全屏
         */
        int CODE_REQUEST_FULLSCREEN_EXIT = -141;

        /**
         * 请求 进度自动更新状态
         */
        int CODE_REQUEST_TIMER_UPDATE_STATE = -150;
    }

}
