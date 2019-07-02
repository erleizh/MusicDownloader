package com.erlei.musicdownloader.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.InputStream;

/**
 * Created by lll on 2018/3/26.
 * Email : lllemail@foxmail.com
 */
@GlideModule
public class AppGlideModule extends com.bumptech.glide.module.AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //使用Okhttp下载图片
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        //不使用清单配置的方式,减少初始化时间
        return false;
    }
}
