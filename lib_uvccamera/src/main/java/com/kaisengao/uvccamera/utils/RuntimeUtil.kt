package com.kaisengao.uvccamera.utils

import android.util.Log
import com.kaisengao.uvccamera.BuildConfig
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * @ClassName: RuntimeUtil
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/6 11:14
 * @Description:
 */
object RuntimeUtil {

    private val TAG: String = RuntimeUtil::class.java.simpleName

    fun runCommand(command: String) {
        debug("execute command start : $command")
        var status = -1
        var process: Process? = null
        var successReader: BufferedReader? = null
        var errorReader: BufferedReader? = null
        var outputStream: DataOutputStream? = null
        var errorMsg: StringBuilder? = null

        try {
            process = Runtime.getRuntime().exec("su")
            successReader = BufferedReader(
                InputStreamReader(process.inputStream)
            )
            errorReader = BufferedReader(
                InputStreamReader(process.errorStream)
            )
            outputStream = DataOutputStream(process.outputStream)
            outputStream.writeBytes(
                """
    $command
    
    """.trimIndent()
            )
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            status = process.waitFor()

            var lineStr: String?
            while (successReader.readLine().also { lineStr = it } != null) {
                debug(" command line item : $lineStr")
            }
            while (errorReader.readLine().also { lineStr = it } != null) {
                errorMsg?.append(lineStr)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (successReader != null) {
                try {
                    successReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (errorReader != null) {
                try {
                    errorReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            process?.destroy()
        }
        debug(
            String.format(
                Locale.CHINA,
                "execute command end,errorMsg:%s,and status %d: ", errorMsg,
                status
            )
        );
    }

    /**
     * DEBUG LOG
     *
     * @param message
     */
    private fun debug(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}