package com.kaisengao.uvccamera.utils

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AlertDialog

/**
 * @ClassName: DialogUtil
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/8 14:45
 * @Description: dialog 工具类
 */
object DialogUtil {

    /**
     * 提示 重启对话框
     */
    fun getRebootDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle("提示")
            .setCancelable(false)
            .setMessage("系统出现错误，请重启手机！")
            .setPositiveButton("重启", null)
            .create().also {
                it.show()
                it.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            }
    }
}