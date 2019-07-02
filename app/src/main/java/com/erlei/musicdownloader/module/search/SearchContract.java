package com.erlei.musicdownloader.module.search;

import android.support.annotation.NonNull;

import com.erlei.musicdownloader.base.BaseBean;
import com.erlei.musicdownloader.base.Contract;
import com.erlei.musicdownloader.http.requests.SearchMusicRequest;
import com.erlei.musicdownloader.http.responses.MusicBean;

import java.util.List;

import io.reactivex.Observable;

public interface SearchContract extends Contract {

    interface View extends Contract.View {

        /**
         * 显示搜索音乐的结果
         *
         * @param musics result
         */
        void showSearchResult(@NonNull List<MusicBean> musics);

        /**
         * 加载更多
         *
         * @param musics result
         */
        void loadMoreResult(@NonNull List<MusicBean> musics);

        /**
         * 加载更多结束
         */
        void loadMoreFinish();

        /**
         * 搜索结果为空
         */
        void showEmptyResult();
    }

    interface Model extends Contract.Model {

        /**
         * 搜索音乐
         *
         * @param request 请求参数
         */
        Observable<BaseBean<List<MusicBean>>> searchMusic(SearchMusicRequest request);

    }

    abstract class Presenter extends Contract.Presenter<SearchContract.View> {

        public Presenter(SearchContract.View view) {
            super(view);
        }

        /**
         * 搜索音乐
         *
         * @param request 请求参数
         */
        public abstract void searchMusic(SearchMusicRequest request);

        /**
         * 加載更多
         *
         * @param request 请求参数
         */
        public abstract void loadMore(SearchMusicRequest request);

        /**
         * 添加到下载列表
         * @param musicBeans 音乐信息
         */
        public abstract void download(List<MusicBean> musicBeans);
    }
}
