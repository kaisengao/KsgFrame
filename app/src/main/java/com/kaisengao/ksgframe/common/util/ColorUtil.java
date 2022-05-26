package com.kaisengao.ksgframe.common.util;

/**
 * @ClassName: ColorText
 * @Author: KaiSenGao
 * @CreateDate: 1/6/21 2:11 PM
 * @Description:
 */
public class ColorUtil {

    /**
     * 百分比色值
     *
     * @param percent 百分比
     * @param color   颜色
     * @return #xxxx
     */
    public static String percentColor(float percent, String color) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 100) {
            percent = 100;
        }
        // 计算百分比色值
        percent = percent / 100;
        percent = Math.round(percent * 100) / 100f;
        int alpha = (int) Math.round(percent * 255);
        String hex = Integer.toHexString(alpha).toUpperCase();
        if (hex.length() == 1) hex = "0" + hex;
        return "#" + hex + color;
    }
}