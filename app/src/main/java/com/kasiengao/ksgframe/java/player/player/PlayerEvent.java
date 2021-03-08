package com.kasiengao.ksgframe.java.player.player;

import com.ksg.ksgplayer.assist.DataInter;

/**
 * @ClassName: PlayerEvent
 * @Author: KaiSenGao
 * @CreateDate: 2020/7/20 10:10
 * @Description:
 */
public interface PlayerEvent extends DataInter.Event {

    /**
     * 直播 前后置摄像头
     */
    int EVENT_CODE_REQUEST_SWITCH_CAMERA = -102;

    /**
     * 直播 美颜 on
     */
    int EVENT_CODE_REQUEST_BEAUTY_ON = -103;

    /**
     * 直播 美颜 off
     */
    int EVENT_CODE_REQUEST_BEAUTY_OFF = -104;

    /**
     * 开始连麦 推流
     */
    int EVENT_CODE_REQUEST_START_LINK_MIC = -105;

    /**
     * 结束连麦 推流
     */
    int EVENT_CODE_REQUEST_STOP_LINK_MIC = -106;

    /**
     * Activity 的旋转方向，配置推流器
     */
    int EVENT_CODE_REQUEST_ROTATION_FOR = -117;

    /**
     * 提交 点赞数量
     */
    int EVENT_CODE_REQUEST_PRAISE_COUNT = -108;

    /**
     * 直播 连麦 on
     */
    int EVENT_CODE_REQUEST_LINK_MIC_ON = -109;

    /**
     * 直播 连麦 off
     */
    int EVENT_CODE_REQUEST_LINK_MIC_OFF = -110;

    /**
     * IM 发送消息
     */
    int EVENT_CODE_REQUEST_IM_MESSAGE = -111;

    /**
     * 历史录像 请求更多IM消息列表数据
     */
    int EVENT_CODE_REQUEST_IM_MESSAGE_LIST = -112;


}
