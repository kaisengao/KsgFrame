package com.kaisengao.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: BaseResult
 * @Author: KaiSenGao
 * @CreateDate: 2020/4/1 14:06
 * @Description: 通用 json 结构
 */
public class BaseResult<T> {

    @SerializedName(value = "code", alternate = {"error_code", "resultcode"})
    private int mCode;
    @SerializedName(value = "message", alternate = {"reason"})
    private String mMessage;
    @SerializedName("result")
    private T mResult;

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public T getResult() {
        return mResult;
    }

    public boolean isSuccess() {
        return getCode() == 200;
    }
}
