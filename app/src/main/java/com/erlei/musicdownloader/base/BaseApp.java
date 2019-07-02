package com.erlei.musicdownloader.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.erlei.musicdownloader.BuildConfig;
import com.erlei.musicdownloader.utils.KV;
import com.erlei.musicdownloader.utils.LifecycleChangeListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


/**
 * Created by lll on 2018/3/22.
 * Email : lllemail@foxmail.com
 */
public class BaseApp extends Application {


    @SuppressLint("StaticFieldLeak")
    private static Context mApp;
    private LifecycleChangeListener mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        KV.init(this);
        Utils.init(this);
        initForegroundOrBackgroundChangeListener();
    }


    /**
     * 初始化前后台变化监听器
     */
    private void initForegroundOrBackgroundChangeListener() {
        mInstance = LifecycleChangeListener.get(this);
        mInstance.addListener(new LifecycleChangeListener.Listener() {
            @Override
            public void onBecameForeground() {
                Logger.d("进入前台");
            }

            @Override
            public void onBecameBackground() {
                Logger.d("进入后台");
            }
        });
    }

    public static Context getContext() {
        return mApp;
    }


    private void initLogger() {
        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .tag("keji")
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(strategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mApp = this;
    }
}
