package com.kasiengao.base.util;

import java.text.DecimalFormat;

/**
 * @ClassName: FileUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/21 14:49
 * @Description:
 */
public class FileUtil {

    /**
     * 将字节 byte 转为对应的合适的单位
     *
     * @return String
     */
    public static String getFileSizeDescription(long size) {
        //定义GB/MB/KB的计算常量
        double GB = 1024.0 * 1024.0 * 1024.0;
        double MB = 1024.0 * 1024.0;
        double KB = 1024.0;
        StringBuilder bytes = new StringBuilder();
        DecimalFormat df = new DecimalFormat("###.00");
        if (size >= GB) {
            double i = (size / GB);
            bytes.append(df.format(i)).append("GB");
        } else if (size >= MB) {
            double i = (size / MB);
            bytes.append(df.format(i)).append("MB");
        } else if (size >= KB) {
            double i = (size / KB);
            bytes.append(df.format(i)).append("KB");
        } else {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }
}
