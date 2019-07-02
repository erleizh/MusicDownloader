package com.erlei.musicdownloader.module.downlaod;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.erlei.musicdownloader.http.RxSchedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressWarnings("unused")
public class SimpleDownloadManager implements DownloadManager {

    private static final SimpleDownloadManager ourInstance = new SimpleDownloadManager();
    private int mBufferSize = 2048;

    public static SimpleDownloadManager getInstance() {
        return ourInstance;
    }

    private OkHttpClient mClient;
    private ConcurrentSkipListMap<DownloadTask, Call> mRunningTasks = new ConcurrentSkipListMap<>();
    private List<DownloadTask> mPendingTasks = new ArrayList<>();
    private List<DownloadTask> mFinishedTasks = new ArrayList<>();

    private SimpleDownloadManager() {
        mClient = new OkHttpClient.Builder().build();
    }


    /**
     * 立即开始执行一个下载任务
     *
     * @param task 任务
     * @return 观察者
     */
    @Override
    public Observable<DownloadTask> download(DownloadTask task) {
        return Observable.just(task)
                .filter(task1 -> mRunningTasks.containsKey(task) || mPendingTasks.contains(task) || mFinishedTasks.contains(task))
                .map(getContentLength())
                .flatMap((Function<DownloadTask, ObservableSource<DownloadTask>>) task12 -> Observable.create(new DownloadSubscribe(task12)))
                .retry(3)
                .compose(managementTaskState())
                .compose(RxSchedulers.io2Main());
    }

    private Function<DownloadTask, DownloadTask> getContentLength() {
        return task -> {
            task.setContentLength(getContentLength(task.getUrl()));
            return task;
        };
    }


    /**
     * 管理task 状态
     */
    private ObservableTransformer<DownloadTask, DownloadTask> managementTaskState() {
        return upstream -> Observable.create(emitter -> upstream.subscribe(new DisposableObserver<DownloadTask>() {
            private DownloadTask mTask;

            @Override
            public void onNext(DownloadTask t) {
                if (mTask == null) {
                    mTask = t;
                    mTask.setState(DownloadTask.State.Running);
                }
                emitter.onNext(t);
            }

            @Override
            public void onError(Throwable e) {
                mTask.putExtra(DownloadTask.ERROR_INFO, ThrowableUtils.getFullStackTrace(e));
                mTask.setState(DownloadTask.State.Failure);
                emitter.onError(e);
            }

            @Override
            public void onComplete() {
                mTask.setState(DownloadTask.State.Success);
                emitter.onComplete();
            }
        }));
    }

    @Override
    public synchronized void cancel(DownloadTask task) {
        if (mRunningTasks.containsKey(task)) {
            Call call = mRunningTasks.remove(task);
            if (call != null && !call.isCanceled()) {
                task.setState(DownloadTask.State.Cancelled);
                call.cancel();
            }
        }
        mPendingTasks.remove(task);
    }

    /**
     * 添加一个下载任务到队列
     *
     * @param tasks task
     */
    @Override
    public synchronized DisposableObserver<DownloadTask> enqueue(DownloadTask... tasks) {
        return Observable.fromArray(tasks)
                .map(getContentLength())
                .retry(3)
                .compose(RxSchedulers.io2Main())
                .subscribeWith(new DisposableObserver<DownloadTask>() {
                    @Override
                    public void onNext(DownloadTask task) {
                        mPendingTasks.add(task);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 如果该任务还没开始执行，那么从队列里面移除，否则不执行任何操作
     *
     * @param task task
     */
    @Override
    public synchronized boolean dequeue(DownloadTask task) {
        return mPendingTasks.remove(task);
    }

    /**
     * @return 获取任务列表
     */
    @Override
    public List<DownloadTask> getDownloadTasks() {
        ArrayList<DownloadTask> tasks = new ArrayList<>();
        tasks.addAll(mRunningTasks.keySet());
        tasks.addAll(mPendingTasks);
        return tasks;
    }

    /**
     * @return 获取正在下载的任务列表
     */
    @Override
    public List<DownloadTask> getRunningTasks() {
        return new ArrayList<>(mRunningTasks.keySet());
    }

    /**
     * @return 获取待执行任务
     */
    @Override
    public List<DownloadTask> getPendingTasks() {
        return new ArrayList<>(mPendingTasks);
    }

    /**
     * @return 获取已下载的任务列表
     */
    @Override
    public List<DownloadTask> getFinishedTasks() {
        return new ArrayList<>(mFinishedTasks);
    }


    /**
     * @param downloadUrl 链接
     * @return 获取下载长度
     */
    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) {
                    return DownloadTask.ERROR_CONTENT_LENGTH;
                } else {
                    long contentLength = body.contentLength();
                    response.close();
                    return contentLength == 0 ? DownloadTask.ERROR_CONTENT_LENGTH : contentLength;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DownloadTask.ERROR_CONTENT_LENGTH;
    }


    private class DownloadSubscribe implements ObservableOnSubscribe<DownloadTask> {
        private final DownloadTask mTask;

        DownloadSubscribe(DownloadTask task) {
            mTask = task;
        }

        @Override
        public void subscribe(ObservableEmitter<DownloadTask> e) throws IOException {
            String url = mTask.getUrl();
            long downloadLength = mTask.getProgress();//已经下载好的长度
            long contentLength = mTask.getContentLength();//文件的总长度

            if (contentLength == 0) {
                contentLength = getContentLength(mTask.getUrl());
                mTask.setContentLength(contentLength);
            }
            //初始进度信息
            e.onNext(mTask);
            Request request = new Request.Builder()
                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
                    .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                    .url(url)
                    .build();
            Call call = mClient.newCall(request);

            toRunningTasks(mTask, call);

            Response response = call.execute();

            File file = new File(mTask.getPath());
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            try {
                ResponseBody body = response.body();
                if (body != null) {
                    is = body.byteStream();
                    fileOutputStream = new FileOutputStream(file, true);
                    byte[] buffer = new byte[mBufferSize];//缓冲数组2kB
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        downloadLength += len;
                        mTask.setProgress(downloadLength);
                        e.onNext(mTask);
                    }
                    fileOutputStream.flush();
                    toFinishedTask(mTask);
                    e.onComplete();//完成
                } else {
                    throw new IllegalStateException("empty response body!");
                }
            } catch (Exception e1) {
                e.onError(e1);
            } finally {
                //关闭IO流
                CloseUtils.closeIO(is, fileOutputStream);
            }
        }


    }

    protected void toRunningTasks(DownloadTask task, Call call) {
        mPendingTasks.remove(task);
        mRunningTasks.put(task, call);
    }

    protected void toFinishedTask(DownloadTask task) {
        mRunningTasks.remove(task);
        mFinishedTasks.add(task);
    }
}
