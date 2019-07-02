package com.erlei.musicdownloader.module.downlaod;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.util.ArrayMap;

import com.erlei.musicdownloader.base.BaseApp;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class SystemDownloadManager implements DownloadManager {

    private static final SystemDownloadManager ourInstance = new SystemDownloadManager();
    private final android.app.DownloadManager mDownloadManager;
    private final ArrayMap<Long, DownloadTask> downloadTasks = new ArrayMap<>();

    public static SystemDownloadManager getInstance() {
        return ourInstance;
    }

    private SystemDownloadManager() {
        mDownloadManager = (android.app.DownloadManager) BaseApp.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 下载文件
     *
     * @param task 任务
     * @return 观察者
     */
    @Override
    public Observable<DownloadTask> download(DownloadTask task) {
        enqueue(task);
        return null;
    }

    /**
     * 取消一个任务
     *
     * @param task task
     */
    @Override
    public void cancel(DownloadTask task) {
        if (downloadTasks.containsValue(task)) {
            for (int i = downloadTasks.keySet().size() - 1; i >= 0; i--) {
                if (task.equals(downloadTasks.valueAt(i))) {
                    downloadTasks.remove(downloadTasks.keyAt(i));
                    --i;
                }
            }
        }
    }

    /**
     * 添加一个下载任务到队列
     *
     * @param tasks tasks
     */
    @Override
    public DisposableObserver<DownloadTask> enqueue(DownloadTask... tasks) {
        for (DownloadTask task : tasks) {
            Request request = new Request(Uri.parse(task.getUrl()));
            request.setTitle(task.getTitle());
            request.setDescription(task.getDescription());
            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
            request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
            File file = new File(task.getPath());
            if (file.exists()) continue;
            request.setDestinationUri(Uri.fromFile(file));
            downloadTasks.put(mDownloadManager.enqueue(request), task);
        }
        return null;
    }

    /**
     * 如果该任务还没开始执行，那么从队列里面移除，否则不执行任何操作
     *
     * @param task task
     * @return 是否成功
     */
    @Override
    public boolean dequeue(DownloadTask task) {
        return false;
    }

    /**
     * @return 获取任务列表
     */
    @Override
    public List<DownloadTask> getDownloadTasks() {
        return null;
    }

    /**
     * @return 获取正在下载的任务列表
     */
    @Override
    public List<DownloadTask> getRunningTasks() {
        return null;
    }

    /**
     * @return 获取待执行任务
     */
    @Override
    public List<DownloadTask> getPendingTasks() {
        return null;
    }

    /**
     * @return 获取已下载的任务列表
     */
    @Override
    public List<DownloadTask> getFinishedTasks() {
        return null;
    }
}
