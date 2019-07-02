package com.erlei.musicdownloader.module.downlaod;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;


public class DownloadService extends Service {


    private static final String EXTRA_TASKS = "extra_tasks";
    private static final String EXTRA_ACTION = "extra_action";
    private DownloadBinder mBinder = new DownloadBinder();
    private Handler mHandler;
    private HandlerThread mHandlerThread;

    public static Intent getIntent(Context context) {
        return new Intent(context, DownloadService.class);

    }

    public static Intent getIntent(Context context, ArrayList<DownloadTask> tasks) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(EXTRA_TASKS, tasks);
        return intent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("DownloadHandlerThread");
        mHandlerThread.start();
        Looper looper = mHandlerThread.getLooper();
        mHandler = new DownloadHandler(looper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<DownloadTask> tasks = (ArrayList<DownloadTask>) intent.getSerializableExtra(EXTRA_TASKS);
        if (tasks != null) mBinder.download(tasks);
        return super.onStartCommand(intent, flags, startId);

    }

    public class DownloadBinder extends Binder {

        public void download(List<DownloadTask> tasks) {

        }
    }

    private class DownloadHandler extends Handler {

        DownloadHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    }
}
