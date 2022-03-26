package com.ksg.ksgplayer.helper;

import android.net.TrafficStats;

import java.text.DecimalFormat;

/**
 * @author kaisengao
 * @create: 2019/2/27 15:13
 * @describe: 系统API 获取网速 帮助类
 */
public class NetSpeedHelper {

    private long lastTotalRxBytes = 0;

    private long lastTimeStamp = 0;

    public String getNetSpeed(int uid) {
        long nowTotalRxBytes = getTotalRxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        // 毫秒转换
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return getNetDescription(speed);
    }

    private long getTotalRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalRxBytes();
    }

    private String getNetDescription(long size) {
        StringBuilder bytes = new StringBuilder();
        DecimalFormat format = new DecimalFormat("###");
        if (size > (1024f * 1024f)) {
            double i = (size / 1024f / 1024f);
            bytes.append(format.format(i)).append("Mb/s");
        } else if (size > 1024) {
            double i = (size / 1024f);
            bytes.append(format.format(i)).append("Kb/s");
        } else {
            bytes.append(format.format((int) size)).append("b/s");
        }
        return bytes.toString();
    }
}
