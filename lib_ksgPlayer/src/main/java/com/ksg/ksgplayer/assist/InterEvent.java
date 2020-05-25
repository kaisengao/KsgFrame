package com.ksg.ksgplayer.assist;

/**
 * @author kaisengao
 * @create: 2019/1/29 17:04
 * @describe:
 */
public interface InterEvent {


    int CODE_REQUEST_START = -66000;

    int CODE_REQUEST_PAUSE = -66001;

    int CODE_REQUEST_OPTION = -66002;

    int CODE_REQUEST_RESUME = -66003;

    int CODE_REQUEST_SEEK = -66005;

    int CODE_REQUEST_STOP = -66007;

    int CODE_REQUEST_RESET = -66009;

    int CODE_REQUEST_RETRY = -660011;

    int CODE_REQUEST_REPLAY = -66013;

    int CODE_REQUEST_PLAY_DATA_SOURCE = -66014;

    int CODE_REQUEST_EVENT_ADD_PRODUCER = -66015;

    int CODE_REQUEST_EVENT_REMOVE_PRODUCER = -66016;

    int CODE_REQUEST_NOTIFY_TIMER = -66017;

    int CODE_REQUEST_STOP_TIMER = -66018;

}
