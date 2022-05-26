package com.kaisengao.base.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.StringRes;

import com.kaisengao.base.factory.AppFactory;


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

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return AppFactory.application().getString(resId, formatArgs);
    }

    public static String format(@StringRes int format, Object... args) {
        return String.format(AppFactory.application().getString(format), args);
    }

    /**
     * 处理字符串字体大小
     *
     * @param text     内容
     * @param start    开始index
     * @param end      结束index
     * @param textSize 文字大小
     */
    public static SpannableString handleTextSize(CharSequence text, int start, int end, float textSize) {
        // 处理字符串
        SpannableString spannableString = new SpannableString(text);
        // 字符串字体处理
        spannableString.setSpan(new AbsoluteSizeSpan((int) textSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 返回处理后的字符串
        return spannableString;
    }

    /**
     * 处理字符串字体颜色
     *
     * @param text  内容
     * @param start 开始index
     * @param end   结束index
     * @param color 文本颜色
     */
    public static SpannableString handleTextColor(String text, int start, int end, int color) {
        // 处理字符串
        SpannableString spannableString = new SpannableString(text);
        // 字符串字体处理
        spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 返回处理后的字符串
        return spannableString;
    }
}