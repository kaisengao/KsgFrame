package com.kaisengao.retrofit.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @ClassName: ParamsUtil
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 19:00
 * @Description:
 */
public class ParamsUtil {

    public static class JsonBuilder {

        private JSONObject mJson;

        public static JsonBuilder create() {
            return new JsonBuilder();
        }

        public JsonBuilder() {
            mJson = new JSONObject();
        }

        public JsonBuilder put(String k, int v) throws JSONException {
            this.mJson.put(k, v);
            return this;
        }

        public JsonBuilder put(String k, long v) throws JSONException {
            this.mJson.put(k, v);
            return this;
        }

        public JsonBuilder put(String k, double v) throws JSONException {
            this.mJson.put(k, v);
            return this;
        }

        public JsonBuilder put(String k, String v) throws JSONException {
            this.mJson.put(k, v);
            return this;
        }

        public JsonBuilder put(String k, JSONObject v) throws JSONException {
            this.mJson.put(k, v);
            return this;
        }

        public String build() {
            return this.mJson.toString();
        }
    }

    public static class HashBuilder {

        private HashMap<String, String> mHashMap;


        public static HashBuilder create() {
            return new HashBuilder();
        }

        public HashBuilder() {
            this.mHashMap = new HashMap<>();
        }

        public HashBuilder put(String k, String v) {
            this.mHashMap.put(k, v);
            return this;
        }

        public HashBuilder put(String k, int v) {
            this.mHashMap.put(k, String.valueOf(v));
            return this;
        }

        public HashBuilder put(String k, long v) {
            this.mHashMap.put(k, String.valueOf(v));
            return this;
        }

        public HashMap<String, String> build() {
            return this.mHashMap;
        }
    }

}
