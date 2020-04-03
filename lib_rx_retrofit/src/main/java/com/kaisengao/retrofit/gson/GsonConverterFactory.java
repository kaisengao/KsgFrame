package com.kaisengao.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @ClassName: GsonConverterFactory
 * @Author: KaiSenGao
 * @CreateDate:  2020/3/31 17:59
 * @Description: 自定义 GsonConverterFactory 主要用于过滤 空对象 与 空数组
 *
 * 这个类只是源代码 并没有做处理
 */
public class GsonConverterFactory extends Converter.Factory {

    public static GsonConverterFactory create() {
        return create(new Gson());
    }

    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    private final Gson mGson;

    private GsonConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("mGson == null");
        }
        this.mGson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(mGson, adapter);
    }
}
