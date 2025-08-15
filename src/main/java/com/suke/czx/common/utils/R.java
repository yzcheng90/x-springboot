package com.suke.czx.common.utils;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String,Object> {
    public R() {
        this.put((String)"code", 0);
        this.put((String)"msg", "success");
    }

    public static R error() {
        return error(500, "服务未知异常");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put((String)"code", code);
        r.put((String)"msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put((String)"msg", msg);
        return r;
    }

    public static R ok(String msg, String data) {
        R r = new R();
        r.put((String)"msg", msg);
        r.put((String)"data", data);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R setPage(Object value) {
        super.put("page", value);
        return this;
    }

    public R setData(Object value) {
        super.put("data", value);
        return this;
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}