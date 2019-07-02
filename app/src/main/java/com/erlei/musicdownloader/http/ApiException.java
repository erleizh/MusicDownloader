package com.erlei.musicdownloader.http;


import android.net.ParseException;

import com.blankj.utilcode.util.Utils;
import com.erlei.musicdownloader.R;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * Created by lll on 2018/7/30
 * Email : lllemail@foxmail.com
 * Describe : 请求出现了异常
 * http://www.w3school.com.cn/tags/html_ref_httpmessages.asp
 */
@SuppressWarnings("all")
public class ApiException extends Throwable {
    public static final int UNKNOWN_ERROR = -1;
    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int PARSE_EXCEPTION = 450;
    public static final int CONNECT_EXCEPTION = 451;

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    private int code;
    private String msg;
    private String mDisplayMsg;

    public ApiException() {
    }

    public ApiException(int code, String message) {
        this.code = code;
        this.msg = message;
        this.mDisplayMsg = message;
    }

    public ApiException(Throwable e) {
        handleError(e);
    }

    private void handleError(Throwable e) {
        initCause(e);
        setStackTrace(e.getStackTrace());
        setMsg(e.getMessage());
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    setDisplayMsg(Utils.getApp().getString(R.string.network_exception));                  //均视为网络错误
                    break;
            }
            setCode(httpException.code());
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            setCode(PARSE_EXCEPTION);
            setDisplayMsg(Utils.getApp().getString(R.string.network_parse_exception));            //均视为解析错误
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
            setCode(CONNECT_EXCEPTION);
            setDisplayMsg(Utils.getApp().getString(R.string.network_connection_failed_please_check_the_network));  //均视为连接错误
        } else {
            setCode(UNKNOWN_ERROR);
            setDisplayMsg(Utils.getApp().getString(R.string.network_unknown_error));                 //未知错误
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDisplayMsg() {
        return mDisplayMsg;
    }

    public void setDisplayMsg(String displayMsg) {
        mDisplayMsg = displayMsg;
    }
}
