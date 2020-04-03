package com.kaisengao.retrofit.exception;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.kaisengao.retrofit.R;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * @ClassName: ExceptionHandle
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:18
 * @Description: 异常信息处理类
 */
public class ExceptionHandle {

    public static String handleException(Context context, Throwable e) {
        String errorMsg = e.getMessage();
        if (e instanceof SocketTimeoutException
                || e instanceof ConnectException) {
            errorMsg = context.getString(R.string.net_time_out);
        } else if (e instanceof HttpException) {
            errorMsg = context.getString(R.string.net_http_abnormal);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {

            if (!TextUtils.isEmpty(e.getMessage())) {
                errorMsg = e.getMessage();
            } else {
                errorMsg = context.getString(R.string.net_json_catch);
            }
        }

        return errorMsg;
    }
}
