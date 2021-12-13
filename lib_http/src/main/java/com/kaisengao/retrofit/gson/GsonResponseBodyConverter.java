package com.kaisengao.retrofit.gson;

import android.text.TextUtils;

import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @ClassName: GsonResponseBodyConverter
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 17:56
 * @Description: 自定义 GsonConverterFactory 主要用于过滤 空对象 与
 * <p>
 * 对返回的数据 进行过滤处理
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final TypeAdapter<T> mAdapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        // 把ResponseBody转为string
        String response = value.string();

        try {

            JSONObject json = new JSONObject(response);

            String result = json.optString("result");

            String object = "{}";

            String empty = "null";

            if (object.equals(result)
                    || empty.equals(result)
                    || TextUtils.isEmpty(result)) {

                // 过滤 空对象 与 空null
                json.remove("result");
            }

            return mAdapter.fromJson(json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JsonIOException("JSON parsing exception！");
        } finally {
            value.close();
        }
    }
}
