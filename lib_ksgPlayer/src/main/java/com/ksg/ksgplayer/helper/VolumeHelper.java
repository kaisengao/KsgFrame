package com.ksg.ksgplayer.helper;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author kaisengao
 * @create: 2019/3/8 13:42
 * @describe: 音量控制 帮助类
 */
public class VolumeHelper {

    /**
     * 媒体
     */
    public final static int TYPE_MUSIC = AudioManager.STREAM_MUSIC;

    /**
     * 警报
     */
    public final static int TYPE_ALARM = AudioManager.STREAM_ALARM;

    /**
     * 铃声
     */
    public final static int TYPE_RING = AudioManager.STREAM_RING;

    /**
     * 伪枚举
     */
    @IntDef({TYPE_MUSIC, TYPE_ALARM, TYPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    /**
     * 调整时显示音量条,就是按音量键出现的那个
     */
    public final static int FLAG_SHOW_UI = AudioManager.FLAG_SHOW_UI;

    /**
     * 调整音量时播放声音
     */
    public final static int FLAG_PLAY_SOUND = AudioManager.FLAG_PLAY_SOUND;

    /**
     * nothing 什么都没有
     */
    public final static int FLAG_NOTHING = 0;

    /**
     * 伪枚举
     */
    @IntDef({FLAG_SHOW_UI, FLAG_PLAY_SOUND, FLAG_NOTHING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FLAG {
    }

    private int mTypeMusic = TYPE_MUSIC;

    private int mNowFlag = FLAG_NOTHING;

    /**
     * 步进值
     */
    private int mVoiceStep100 = 1;

    private AudioManager mAudioManager;

    /**
     * 初始化，获取音量管理者
     *
     * @param context 上下文
     */
    public VolumeHelper(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 获取音量最大值
     */
    public int getSystemMaxVolume() {
        return mAudioManager.getStreamMaxVolume(mTypeMusic);
    }

    /**
     * 获取当前音量
     */
    public int getSystemCurrentVolume() {
        return mAudioManager.getStreamVolume(mTypeMusic);
    }

    /**
     * 以0-100为范围，获取当前的音量值
     *
     * @return 获取当前的音量值
     */
    public int get100CurrentVolume() {
        return 100 * getSystemCurrentVolume() / getSystemMaxVolume();
    }

    /**
     * 修改步进值
     *
     * @param step step
     * @return this
     */
    public VolumeHelper setVoiceStep100(int step) {
        mVoiceStep100 = step;
        return this;
    }

    /**
     * 改变当前的模式，对全局API生效
     *
     * @param type 音量类型
     * @return VolumeHelper
     */
    public VolumeHelper setAudioType(@TYPE int type) {
        mTypeMusic = type;
        return this;
    }

    /**
     * 改变当前FLAG，对全局API生效
     *
     * @param flag 调节音量提示类型
     * @return VolumeHelper
     */
    public VolumeHelper setFlag(@FLAG int flag) {
        mNowFlag = flag;
        return this;
    }

    /**
     * 增加音量，调出系统音量控制
     */
    public VolumeHelper addVoiceSystem() {
        mAudioManager.adjustStreamVolume(mTypeMusic, AudioManager.ADJUST_RAISE, mNowFlag);
        return this;
    }

    /**
     * 降低音量，调出系统音量控制
     */
    public VolumeHelper subVoiceSystem() {
        mAudioManager.adjustStreamVolume(mTypeMusic, AudioManager.ADJUST_LOWER, mNowFlag);
        return this;
    }


    /**
     * 调整音量
     *
     * @param num 数值
     * @return 改完后的音量值
     */
    public int setVoice(int num) {
        mAudioManager.setStreamVolume(mTypeMusic, num, mNowFlag);
        return num;
    }

    /**
     * 调整音量，自定义
     *
     * @param num 0-100
     * @return 改完后的音量值
     */
    public int setVoice100(int num) {
        int a = (int) Math.ceil((num) * getSystemMaxVolume() * 0.01);
        a = Math.max(a, 0);
        a = Math.min(a, 100);
        mAudioManager.setStreamVolume(mTypeMusic, a, 0);
        return num;
    }

    /**
     * 步进加，步进值可修改
     * 0——100
     *
     * @return 改完后的音量值
     */
    public int addVoice100() {
        int a = (int) Math.ceil((mVoiceStep100 + get100CurrentVolume()) * getSystemMaxVolume() * 0.01);
        a = Math.max(a, 0);
        a = Math.min(a, 100);
        mAudioManager.setStreamVolume(mTypeMusic, a, mNowFlag);
        return get100CurrentVolume();
    }

    /**
     * 步进减，步进值可修改
     * 0——100
     *
     * @return 改完后的音量值
     */
    public int subVoice100() {
        int a = (int) Math.floor((get100CurrentVolume() - mVoiceStep100) * getSystemMaxVolume() * 0.01);
        a = Math.max(a, 0);
        a = Math.min(a, 100);
        mAudioManager.setStreamVolume(mTypeMusic, a, mNowFlag);
        return get100CurrentVolume();
    }
}
