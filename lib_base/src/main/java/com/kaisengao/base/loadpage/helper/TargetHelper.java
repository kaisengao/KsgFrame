package com.kaisengao.base.loadpage.helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: TargetHelper
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 10:10
 * @Description:
 */
public class TargetHelper {

    /**
     * 获取Target的父View
     *
     * @param target 绑定的目标
     */
    public static ViewGroup getTargetParent(Object target) {
        if (target instanceof Activity) {
            return ((Activity) target).findViewById(android.R.id.content);
        } else if (target instanceof View) {
            return (ViewGroup) ((View) target).getParent();
        }
        throw new NullPointerException("Get target parent not found!");
    }

    /**
     * 获取Target在父View的坐标
     *
     * @param target 绑定的目标
     */
    public static int getTargetIndexOfChild(Object target, ViewGroup targetParent) {
        if (target instanceof Activity) {
            return 0;
        } else if (target instanceof View) {
            return targetParent.indexOfChild((View) target);
        }
        throw new NullPointerException("Get target IndexOfChild not found!");
    }
}