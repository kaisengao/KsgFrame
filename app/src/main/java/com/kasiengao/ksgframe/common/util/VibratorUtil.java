package com.kasiengao.ksgframe.common.util;

import android.app.Service;
import android.os.Vibrator;

import com.kasiengao.ksgframe.factory.AppFactory;

/**
 * @ClassName: VibratorUtil
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/1 19:09
 * @Description:
 */
public class VibratorUtil {

    /**
     * 震动
     *
     * @param milliseconds 震动的时长
     */
    public static void vibrate(long milliseconds) {
        Vibrator service = (Vibrator) AppFactory.application().getSystemService(Service.VIBRATOR_SERVICE);
        service.vibrate(milliseconds);
    }
}