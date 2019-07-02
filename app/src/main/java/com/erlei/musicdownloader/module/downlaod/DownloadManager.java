package com.erlei.musicdownloader.module.downlaod;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public interface DownloadManager {

    /**
     * 下载文件
     *
     * @param task 任务
     * @return 观察者
     */
    Observable<DownloadTask> download(DownloadTask task);

    /**
     * 取消一个任务
     *
     * @param task task
     */
    void cancel(DownloadTask task);

    /**
     * 添加一个下载任务到队列
     *
     * @param tasks tasks
     */
    DisposableObserver<DownloadTask> enqueue(DownloadTask... tasks);

    /**
     * 如果该任务还没开始执行，那么从队列里面移除，否则不执行任何操作
     *
     * @param task task
     * @return 是否成功
     */
    boolean dequeue(DownloadTask task);

    /**
     * @return 获取任务列表
     */
    List<DownloadTask> getDownloadTasks();

    /**
     * @return 获取正在下载的任务列表
     */
    List<DownloadTask> getRunningTasks();

    /**
     * @return 获取待执行任务
     */
    List<DownloadTask> getPendingTasks();

    /**
     * @return 获取已下载的任务列表
     */
    List<DownloadTask> getFinishedTasks();
}
