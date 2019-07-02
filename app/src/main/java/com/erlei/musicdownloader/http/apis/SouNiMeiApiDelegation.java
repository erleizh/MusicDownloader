package com.erlei.musicdownloader.http.apis;

import com.blankj.utilcode.util.Utils;
import com.erlei.musicdownloader.http.RetrofitUtil;
import com.erlei.musicdownloader.module.search.engine.sounimei.SouNiMeiSearchRequest;
import com.erlei.musicdownloader.module.search.engine.sounimei.SouNiMeiSearchResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class SouNiMeiApiDelegation {


    //    private static final String URL = "http://music.sonimei.cn/";
    static final String URL = "http://music.cccyun.cc/";

    private static SouNiMeiApi mSouNiMeiApi;

    static {
        OkHttpClient.Builder builder = RetrofitUtil.getDefaultClient().newBuilder();
        builder.cache(new Cache(Utils.getApp().getCacheDir(), 1000 * 50));
        builder.connectTimeout(8, TimeUnit.SECONDS);
        mSouNiMeiApi = RetrofitUtil.getService(SouNiMeiApi.class, URL, builder.build());
    }

    /**
     * 搜你妹音乐搜索接口
     */
    public static Observable<SouNiMeiSearchResponse> search(SouNiMeiSearchRequest request) {
        return mSouNiMeiApi.search(request.getFilter(), request.getInput(), request.getType(), request.getPage());
    }


}

@SuppressWarnings({"unused"})
interface SouNiMeiApi {


    /**
     * 搜你妹音乐搜索接口
     *
     * @param filter 过滤器
     * @param input  输入关键字
     * @param type   类型
     * @param page   page
     */
    @FormUrlEncoded
    @Headers({
            "Accept:application/json, text/javascript, */*; q=0.01",
            "X-Requested-With:XMLHttpRequest",
    })
    @POST(SouNiMeiApiDelegation.URL)
    Observable<SouNiMeiSearchResponse> search(
            @Field("filter") String filter,
            @Field("input") String input,
            @Field("type") String type,
            @Field("page") int page
    );

}
