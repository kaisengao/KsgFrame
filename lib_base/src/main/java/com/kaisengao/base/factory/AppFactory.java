package com.kaisengao.base.factory;


import com.kaisengao.base.BaseApplication;

/**
 * @ClassName: AppFactory
 * @Author: KaiSenGao
 * @CreateDate: 2020/10/21 14:29
 * @Description: App工厂帮助类
 */
public class AppFactory {

    /**
     * 初始化的一些操作
     */
    public static void init() {

    }

    /**
     * 返回全局的Application
     *
     * @return BaseApplication
     */
    public static BaseApplication application() {
        return BaseApplication.getInstance();
    }
}