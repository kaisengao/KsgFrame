package com.kaisengao.retrofit.factory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: FromJsonFactory
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 20:01
 * @Description: 解析泛型Json
 */
public class FromJsonFactory<T> {

    public T fromJson(String json) {
        try {

            Gson gson = GsonBuilderFactory.getInstance().getGson();

            Type type = getType();

            if (type == String.class) {
                return (T) json;
            } else if (type == List.class) {
                // list没有指定泛型类型的
                return (T) gson.fromJson(json, type);
            } else if (type == Map.class) {
                // map没有指定泛型类型的
                return (T) gson.fromJson(json, type);
            } else if (type instanceof ParameterizedType && ((ParameterizedType) type).getActualTypeArguments().length == 1) {

                // List指定泛型类型的
                return (T) gson.fromJson(json, type);
            } else if (type instanceof ParameterizedType && ((ParameterizedType) type).getActualTypeArguments().length == 2) {
                // map指定泛型类型的
                return (T) gson.fromJson(json, type);
            } else {
                // 其它单个对象类型
                return (T) gson.fromJson(json, type);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Type getType() {
        ParameterizedType genType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genType).getActualTypeArguments();
        return actualTypeArguments[0];
    }
}
