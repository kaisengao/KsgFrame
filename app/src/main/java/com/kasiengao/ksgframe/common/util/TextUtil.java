package com.kasiengao.ksgframe.common.util;

import androidx.annotation.StringRes;

import com.kasiengao.ksgframe.factory.AppFactory;


/**
 * @ClassName: TextUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/10/21 14:29
 * @Description:
 */
public class TextUtil {

    public static String getString(@StringRes int resId) {
        return AppFactory.application().getString(resId);
    }
}