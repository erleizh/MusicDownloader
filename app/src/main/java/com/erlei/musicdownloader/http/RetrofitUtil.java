package com.erlei.musicdownloader.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.blankj.utilcode.util.DeviceUtils;
import com.erlei.musicdownloader.BuildConfig;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings({"unchecked", "unused", "WeakerAccess"})
public class RetrofitUtil {

    private static ArrayMap<String, Object> sCacheService = new ArrayMap<>();
    private static OkHttpClient sDefaultClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new GlobalParamsInterceptor());
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LoggingInterceptor.Builder()
                    .loggable(true)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("HTTP-Request")
                    .response("HTTP-Response")
                    .build());
            builder.hostnameVerifier((hostname, session) -> true);
        }
        sDefaultClient = builder.build();
    }

    public static OkHttpClient getDefaultClient() {
        return sDefaultClient;
    }

    public static <S> S getService(Class<S> clazz, String url) {
        return getService(clazz, url, getDefaultClient());
    }


    public static <S> S getService(Class<S> clazz, String url, OkHttpClient client) {
        String cacheKey = getCacheKey(clazz, url, client);
        if (sCacheService.containsKey(cacheKey)) {
            return (S) sCacheService.get(cacheKey);
        } else {
            S service = createService(clazz, url, client);
            sCacheService.put(cacheKey, service);
            return service;
        }
    }

    private static <S> S createService(Class<S> clazz, String url, OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder();
        if (!TextUtils.isEmpty(url)) builder.baseUrl(url);
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(client);
        Retrofit retrofit = builder.build();
        return retrofit.create(clazz);
    }

    private static <S> String getCacheKey(Class<S> clazz, String url, OkHttpClient client) {
        return clazz.getName() + url + client.hashCode();
    }

    private static class GlobalParamsInterceptor implements Interceptor {

        private static final String DEVICE_ID = DeviceUtils.getAndroidID();
        private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.header("deviceId", DEVICE_ID);
            builder.header("User-Agent", USER_AGENT);
            Request request = builder.build();
            return chain.proceed(request);
        }
    }
}
