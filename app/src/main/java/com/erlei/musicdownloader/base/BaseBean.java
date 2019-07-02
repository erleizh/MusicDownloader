package com.erlei.musicdownloader.base;


import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Created by lll on 18-3-24.
 * Email : lllemail@foxmail.com
 * Describe : 网络请求基本数据结构
 */
@Keep
public class BaseBean<T> implements Serializable {
    int code = -1;
    String message = "";
    T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
