package com.kaisengao.base.util;

import java.util.Locale;

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
        double newNum = num / 10000.0;
        String numStr = String.format(Locale.getDefault(), "%." + 1 + "f", newNum);
        return numStr + unit;
    }
}