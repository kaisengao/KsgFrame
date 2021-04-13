package com.kaisengao.uvccamera.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * @ClassName: FileUtil
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/8 14:00
 * @Description:
 */
object FileUtil {


    /**
     * 解析名称
     *
     * @param path path
     * @return 返回名称
     */
    fun parseName(path: String): String {
        return path.substring(path.lastIndexOf("/") + 1)
    }

    /**
     * 获取app私有的目录 /storage/emulated/0/Android/data/.......
     */
    fun getFileDir(context: Context, type: String, path: String): String {
        return context.getExternalFilesDir(type)!!.absolutePath + "/" + path
    }

    /**
     * 获取当前目录下所有的mp4文件
     *
     * @param fileDir 目录
     */
    fun getFileVideos(fileDir: String): List<String> {
        val pathList: ArrayList<String> = ArrayList()
        val file = File(fileDir)
        val subFile: Array<File>? = file.listFiles()
        for (iFileLength in subFile?.indices!!) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory) {
                val filename: String = subFile[iFileLength].name
                // 判断是否为MP4结尾
                if (filename.trim { it <= ' ' }.toLowerCase(Locale.ROOT).endsWith(".mp4")) {
                    pathList.add(fileDir + filename)
                }
            }
        }
        return pathList
    }

    /***
     * 获取 视频缩略图
     *
     * @param filePath 视频资源的路径
     * @return 返回缩略图的Bitmap对象
     */
    fun getFileVideoCover(filePath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(filePath)
            bitmap = retriever.frameAtTime
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: RuntimeException) {
            e.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return true/false
     */
    fun deleteFile(filePath: String): Boolean {
        val status: Boolean
        val checker = SecurityManager()
        val file = File(filePath)
        status = if (file.exists()) {
            checker.checkDelete(file.toString())
            if (file.isFile) {
                try {
                    file.delete()
                    true
                } catch (se: SecurityException) {
                    se.printStackTrace()
                    false
                }
            } else {
                false
            }
        } else {
            false
        }
        return status
    }
}