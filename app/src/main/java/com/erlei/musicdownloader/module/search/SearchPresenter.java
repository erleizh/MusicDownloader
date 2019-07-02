package com.erlei.musicdownloader.module.search;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;

import com.erlei.musicdownloader.R;
import com.erlei.musicdownloader.base.BaseBean;
import com.erlei.musicdownloader.callback.SimpleObserver;
import com.erlei.musicdownloader.http.ApiException;
import com.erlei.musicdownloader.http.RxSchedulers;
import com.erlei.musicdownloader.http.requests.SearchMusicRequest;
import com.erlei.musicdownloader.http.responses.MusicBean;
import com.erlei.musicdownloader.module.downlaod.DownloadService;
import com.erlei.musicdownloader.module.downlaod.DownloadTask;
import com.erlei.musicdownloader.module.downlaod.SystemDownloadManager;
import com.erlei.musicdownloader.module.search.engine.sounimei.SouNiMeiSearchEngine;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter extends SearchContract.Presenter {

    private SearchContract.Model mEngine;
    private int mPage;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadBinder = null;
        }
    };
    private DownloadService.DownloadBinder mDownloadBinder;

    SearchPresenter(SearchContract.View view) {
        super(view);
        mEngine = new SouNiMeiSearchEngine();
    }

    SearchPresenter(SearchContract.Model model, SearchContract.View view) {
        super(view);
        mEngine = model;
    }

    /**
     * 搜索音乐
     *
     * @param request 请求参数
     */
    @Override
    public void searchMusic(SearchMusicRequest request) {
        getView().showLoading(true);
        request.setPage(mPage = 1);
        addDisposable(mEngine.searchMusic(request)
                .compose(RxSchedulers.io2Main())
                .subscribeWith(new SimpleObserver<BaseBean<List<MusicBean>>>() {
                    @Override
                    public void onSuccess(BaseBean<List<MusicBean>> o) {
                        super.onSuccess(o);
                        if (o.getData().isEmpty()) {
                            getView().showEmptyResult();
                        } else {
                            getView().showSearchResult(o.getData());
                        }
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        super.onFailure(e);
                        getView().showEmptyResult();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        getView().showLoading(false);
                    }
                }));
    }

    @Override
    public void loadMore(SearchMusicRequest request) {
        request.setPage(++mPage);
        addDisposable(mEngine.searchMusic(request)
                .compose(RxSchedulers.io2Main())
                .subscribeWith(new SimpleObserver<BaseBean<List<MusicBean>>>() {
                    @Override
                    public void onSuccess(BaseBean<List<MusicBean>> o) {
                        super.onSuccess(o);
                        if (o.getData().isEmpty()) {
                            getView().loadMoreFinish();
                        } else {
                            getView().loadMoreResult(o.getData());
                        }
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        super.onFailure(e);
                        getView().loadMoreResult(new ArrayList<>(0));
                    }
                }));
    }

    /**
     * 如果正在DownloadService 已经启动 ，添加到下载列表，否则启动下载器
     *
     * @param musicBeans 音乐信息
     */
    @Override
    public void download(List<MusicBean> musicBeans) {
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        ArrayList<DownloadTask> tasks = DownloadTask.create(dir, musicBeans);
        DownloadTask[] array = tasks.toArray(new DownloadTask[0]);
        getView().showToastMessage(getContext().getString(R.string.format_start_download, array.length));
        SystemDownloadManager.getInstance().enqueue(array);
//        if (mDownloadBinder == null) {
//            mContext.bindService(DownloadService.getIntent(mContext, DownloadTask.create(dir, musicBeans)), mServiceConnection, Context.BIND_AUTO_CREATE);
//        } else {
//            mDownloadBinder.download(DownloadTask.create(dir, musicBeans));
//        }
    }

    @Override
    public void onDetached() {
        super.onDetached();
        if (mDownloadBinder != null) {
            mContext.unbindService(mServiceConnection);
            mDownloadBinder = null;
        }
    }
}
