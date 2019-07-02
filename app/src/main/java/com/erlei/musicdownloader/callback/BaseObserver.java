package com.erlei.musicdownloader.callback;


import com.erlei.musicdownloader.base.BaseBean;
import com.erlei.musicdownloader.http.ApiException;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by lll  on 2018/3/26 .
 * Email : lllemail@foxmail.com
 * Describe : 对网络请求观察者的进一步封装 ,
 */
public abstract class BaseObserver<T> extends DisposableObserver<T> {

    private T mResponse;

    @Override
    public void onNext(T t) {
        mResponse = t;
    }

    /**
     * 請求出現異常
     *
     * @param e 異常信息
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        onFailure(new ApiException(e));
    }

    @Override
    public void onComplete() {
        if (mResponse instanceof BaseBean) {
            BaseBean bean = (BaseBean) mResponse;
            if (isSuccess(bean)) {
                onSuccess(mResponse);
            } else {
                onFailure(new ApiException(bean.getCode(), bean.getMessage()));
            }
        } else {
            onSuccess(mResponse);
        }

        onFinish();
    }

    /**
     * 请求完毕，不论成功或失败
     */
    public void onFinish() {

    }

    protected boolean isSuccess(BaseBean bean) {
        return bean.getCode() == 200 && bean.getData() != null;
    }

    /**
     * 请求成功 , 服务器返回数据
     */
    public abstract void onSuccess(T t);

    /**
     * 请求成功 , 服务器返回错误数据
     *
     * @param e 错误信息
     */
    public abstract void onFailure(ApiException e);
}
