package com.ksg.ksgplayer.assist;

public interface DataInter {

    interface Event extends InterEvent {

        /**
         * 返回事件
         */
        int EVENT_CODE_REQUEST_BACK = -100;

        /**
         * 全屏切换事件
         */
        int EVENT_CODE_REQUEST_TOGGLE_SCREEN = -101;

        /**
         * 声音开关事件
         */
        int EVENT_CODE_REQUEST_VOLUME_ALTER = -102;
    }

    interface Key extends InterKey {

        /**
         * 全屏切换
         */
        String KEY_IS_LANDSCAPE = "isLandscape";

        /**
         * 声音开关
         */
        String KEY_VOLUME_ALTER = "volumeAlter";

        /**
         * Controller 状态
         */
        String KEY_CONTROLLER_STATUS = "controllerStatus";

        /**
         * Controller 播放状态
         */
        String KEY_CONTROLLER_PLAY_STATUS = "controllerPlayStatus";
    }

    interface ReceiverKey {

        String KEY_GESTURE_COVER = "gesture_cover";

        String KEY_LOADING_COVER = "loading_cover";

        String KEY_CONTROLLER_COVER = "controller_cover";

    }

    interface PrivateEvent {

        int EVENT_CODE_GESTURE_SLIDE_SEEK = -201;
    }

}
