package com.kasiengao.ksgframe.common.util;

import androidx.annotation.StringRes;

import com.kaisengao.base.BaseApplication;


/**
 * @ClassName: TextUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/10/21 14:29
 * @Description:
 */
public class TextUtil {

    public static String getString(@StringRes int resId) {
        return BaseApplication.getInstance().getString(resId);
    }
}