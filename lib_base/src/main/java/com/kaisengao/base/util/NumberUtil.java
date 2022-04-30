package com.kaisengao.base.util;

/**
 * @ClassName: NumberUtil
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 11:30
 * @Description:
 */
public class NumberUtil {

    /**
     * 转换数字以万为单位
     *
     * @param num 数字
     */
    public static String formatBigNum(int num) {
        if (num < 10000) {
            return String.valueOf(num);
        }
        String unit = "万";
        int newNum = num / 10000;
        return newNum + unit;
    }
}