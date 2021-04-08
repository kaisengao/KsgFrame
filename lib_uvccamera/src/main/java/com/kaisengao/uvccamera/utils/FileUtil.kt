package com.kaisengao.uvccamera.utils

import android.content.Context

/**
 * @ClassName: FileUtil
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/8 14:00
 * @Description:
 */
object FileUtil {

    /**
     * 获取app私有的目录 /storage/emulated/0/Android/data/.......
     */
    fun getFileDir(context: Context, type: String, path: String): String {
        return context.getExternalFilesDir(type)!!.absolutePath + "/" + path
    }
}