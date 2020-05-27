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
    }

    interface Key extends InterKey {

        String KEY_IS_LANDSCAPE = "isLandscape";
    }

    interface ReceiverKey {

        String KEY_LOADING_COVER = "loading_cover";

        String KEY_CONTROLLER_COVER = "controller_cover";

    }

    interface PrivateEvent {

        int EVENT_CODE_UPDATE_SEEK = -201;
    }

}
