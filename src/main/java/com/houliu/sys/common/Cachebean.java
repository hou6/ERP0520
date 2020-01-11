package com.houliu.sys.common;

import com.alibaba.fastjson.JSON;

/**
 * @author houliu
 * @create 2020-01-12 00:09
 */


public class Cachebean {

    private String key;
    private Object value;

    public Cachebean() {
    }

    public Cachebean(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return JSON.toJSON(value).toString();
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
