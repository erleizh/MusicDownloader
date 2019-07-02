package com.erlei.musicdownloader.module.downlaod;

import com.erlei.musicdownloader.http.responses.MusicBean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadTask<T extends Serializable> implements Serializable {

    public static ArrayList<DownloadTask> create(String dir, List<MusicBean> musicBeans) {
        ArrayList<DownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < musicBeans.size(); i++) {
            tasks.add(DownloadTask.form(dir,musicBeans.get(i)));
        }
        return tasks;
    }

    private static DownloadTask form(String dir, MusicBean musicBean) {
        DownloadTask<MusicBean> downloadTask = new DownloadTask<>();
        downloadTask.setObj(musicBean);
        downloadTask.setUrl(musicBean.getUrl());
        downloadTask.setPath(new File(dir, musicBean.getTitle() + ".mp3").getAbsolutePath());
        downloadTask.setTitle(musicBean.getTitle());
        return downloadTask;
    }



    public static final long ERROR_CONTENT_LENGTH = -1;
    protected static final String ERROR_INFO = DownloadTask.class.getSimpleName() + "_error_msg";
    private String url;
    private long contentLength;
    private long progress;
    private String path;
    private String title;
    private String description;
    private T obj;
    private State mState;
    private HashMap<String, String> extras = new HashMap<>();

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getContentLength() {
        return contentLength;
    }

    protected void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getProgress() {
        return progress;
    }

    protected void setProgress(long progress) {
        this.progress = progress;
    }

    public String getPath() {
        return path;
    }

    public State getState() {
        return mState;
    }

    protected void setState(State state) {
        mState = state;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }


    public String getExtra(String key) {
        return extras.get(key);
    }

    public String removeExtra(String key) {
        return extras.remove(key);
    }

    public void putExtra(String key, String value) {
        extras.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadTask that = (DownloadTask) o;

        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }


    public enum State {
        /**
         * 下载完成
         */
        Success,

        /**
         * 下载中
         */
        Running,

        /**
         * 等待中
         */
        Pending,

        /**
         * 下载出现错误
         */
        Failure,

        /**
         * 已取消
         */
        Cancelled

    }
}
