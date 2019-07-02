package com.erlei.musicdownloader.module.search.engine.sounimei;

import com.erlei.musicdownloader.base.BaseBean;
import com.erlei.musicdownloader.http.apis.SouNiMeiApiDelegation;
import com.erlei.musicdownloader.http.requests.SearchMusicRequest;
import com.erlei.musicdownloader.http.responses.MusicBean;
import com.erlei.musicdownloader.module.search.SearchContract;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by lll on 2019/4/12
 * Email : lllemail@foxmail.com
 * Describe : 使用搜你妹搜索音乐
 * http://music.sonimei.cn/
 */
public class SouNiMeiSearchEngine implements SearchContract.Model {

    /**
     * 搜索音乐
     *
     * @param request 请求参数
     */
    @Override
    public Observable<BaseBean<List<MusicBean>>> searchMusic(SearchMusicRequest request) {
        return SouNiMeiApiDelegation.search(SouNiMeiSearchRequest.from(request)).map(souNiMeiSearchResponse -> {
            BaseBean<List<MusicBean>> bean = new BaseBean<>();
            bean.setCode(souNiMeiSearchResponse.getCode());
            bean.setMessage(souNiMeiSearchResponse.getError());
            bean.setData(souNiMeiSearchResponse.getData());
            return bean;
        });
    }
}
