package com.kaisengao.retrofit.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * @ClassName: GsonBuilderFactory
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 17:57
 * @Description: GsonBuilderFactory
 */
public class GsonBuilderFactory {

    private static GsonBuilderFactory instance;

    private final Gson mGson;

    public static GsonBuilderFactory getInstance() {
        if (instance == null) {
            synchronized (GsonBuilderFactory.class) {
                if (instance == null) {
                    instance = new GsonBuilderFactory();
                }
            }
        }
        return instance;
    }

    private GsonBuilderFactory() {

        this.mGson = createBuilder().create();
    }

    private GsonBuilder createBuilder() {
        return new GsonBuilder();
    }

    public Gson getGson() {
        return mGson;
    }

    public <T> T fromJson(String string, Class<T> tClass) {
        return this.mGson.fromJson(string, tClass);
    }

    public <T> T fromJson(String json, Type type) {
        return this.mGson.fromJson(json, type);
    }
}
