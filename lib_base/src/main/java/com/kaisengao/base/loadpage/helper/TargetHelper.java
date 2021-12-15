package com.kaisengao.base.loadpage.helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.kaisengao.base.loadpage.widget.LoadContainer;

/**
 * @ClassName: TargetHelper
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 10:10
 * @Description:
 */
public class TargetHelper {

    /**
     * 获取Load容器视图
     *
     * @param target 绑定的目标
     */
    public static LoadContainer getLoadContainer(Object target) {
        if (target instanceof Activity) {
            return createAcLoadContainer(target);
        } else if (target instanceof View) {
            return createViewLoadContainer(target);
        }
        throw new NullPointerException("Load view container not found!");
    }

    /**
     * 创建AC的LoadContainer
     *
     * @param target 绑定的目标
     * @return Load容器视图
     */
    private static LoadContainer createAcLoadContainer(Object target) {
        int childIndex = 0;
        // 获取绑定目标的父View
        ViewGroup parentView = ((Activity) target).findViewById(android.R.id.content);
        // 获取绑定目标的View
        View targetView = parentView.getChildAt(childIndex);
        // 创建LoadContainer
        return createLoadContainer(parentView, targetView, childIndex);
    }

    /**
     * 创建View的LoadContainer
     *
     * @param target 绑定的目标
     * @return Load容器视图
     */
    private static LoadContainer createViewLoadContainer(Object target) {
        int childIndex = 0;
        // 获取绑定目标的View
        View targetView = (View) target;
        // 获取绑定目标的父View
        ViewGroup parentView = (ViewGroup) (targetView.getParent());
        // 寻找绑定目标在父View中的坐标
        int childCount = parentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (parentView.getChildAt(i) == targetView) {
                childIndex = i;
                break;
            }
        }
        // 创建LoadContainer
        return createLoadContainer(parentView, targetView, childIndex);
    }

    /**
     * 创建LoadContainer
     *
     * @param parentView 绑定的目标的父View
     * @param targetView 绑定的目标的View
     * @param childIndex 在父View中的坐标
     * @return Load容器视图
     */
    private static LoadContainer createLoadContainer(ViewGroup parentView, View targetView, int childIndex) {
        // 将目标从父View中移除
        parentView.removeView(targetView);
        // 创建Load容器视图
        LoadContainer container = new LoadContainer(targetView.getContext(), targetView);
        // 将容器添加到父View中（相当于替换原View）
        parentView.addView(container, childIndex, targetView.getLayoutParams());
        // Return
        return container;
    }
}