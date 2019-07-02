package com.erlei.musicdownloader.callback;

import android.app.Activity;
import android.support.annotation.IntRange;

import com.blankj.utilcode.util.ActivityUtils;
import com.erlei.musicdownloader.base.Contract;
import com.erlei.musicdownloader.http.ApiException;
import com.erlei.musicdownloader.BuildConfig;

/**
 * Created by lll on 2018/3/27.
 * Email : lllemail@foxmail.com
 */
public abstract class SimpleObserver<T> extends BaseObserver<T> {

    private int mMsgMethod;

    @Override
    public void onSuccess(T o) {

    }

    @Override
    public void onFailure(ApiException e) {
        onFailure(e.getCode(), BuildConfig.DEBUG ? e.getMsg() : e.getDisplayMsg());
    }

    public void onFailure(int code, String msg) {
        showMessage(msg);
    }

    private void showMessage(String msg) {
        if (mMsgMethod == 0) {
            showToast(msg);
        } else {
            showDialog(msg);
        }
    }

    /**
     * @param method 显示消息提示的方式  0  Toast , 1  Dialog
     */
    protected void setMessageMethod(@IntRange(from = 0, to = 1) int method) {
        if (method < 0) mMsgMethod = 0;
        if (mMsgMethod > 1) mMsgMethod = 1;
        mMsgMethod = method;
    }

    protected void showDialog(String msg) {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity != null && topActivity instanceof Contract.View) {
            ((Contract.View) topActivity).showDialogMessage(msg);
        }
    }

    protected void showToast(String msg) {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity != null && topActivity instanceof Contract.View) {
            ((Contract.View) topActivity).showToastMessage(msg);
        }
    }
}
