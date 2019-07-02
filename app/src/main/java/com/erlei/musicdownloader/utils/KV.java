package com.erlei.musicdownloader.utils;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.erlei.musicdownloader.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * Created by lll on 2018/11/1
 * Email : lllemail@foxmail.com
 * Describe : key-value 存储组件 ，包装了Preferences, 支持存储对象
 */
public class KV {

    private static final String LAST_LOGIN_ID = "last_login_id";
    private static KV defaultKV;
    private static KV currentKV;
    private static Application SApplication;


    private final Preferences mPreferences;
    private final Gson mGson;

    private KV(Preferences preferences) {
        mPreferences = preferences;
        mGson = new GsonBuilder().create();
    }

    public static void init(Application app) {
        SApplication = app;
        if (defaultKV == null) {
            defaultKV = new KV(new Preferences(app.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)));
            if (defaultKV.contains(LAST_LOGIN_ID)) {
                login(defaultKV.getString(LAST_LOGIN_ID));
            }
        }
    }

    /**
     * 获取默认的存储库，
     */
    public static KV getDefault() {
        return defaultKV;
    }

    public static boolean isLogin() {
        return currentKV != null;
    }

    /**
     * 登录 ， 如果要分用户
     */
    public static void login(String id) {
        if (currentKV == null) {
            defaultKV.putString(LAST_LOGIN_ID, id);
            currentKV = new KV(new Preferences(SApplication.getSharedPreferences("keji_" + id, Context.MODE_PRIVATE)));
        }
    }

    /**
     * 退出登录 ，调用之后 getCurrent() 返回 null
     */
    public static void logout() {
        if (currentKV != null) {
            currentKV = null;
        }
    }

    /**
     * 获取当前登录用户的存储库
     */
    public static KV getCurrent() {
        if (currentKV == null) throw new IllegalStateException("not login");
        return currentKV;
    }


    public Map<String, ?> getAll() {
        return mPreferences.get();
    }


    @Nullable
    public String getString(String key) {
        return mPreferences.getString(key, null);
    }

    @Nullable
    public String getString(String key, @Nullable String defValue) {
        return mPreferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mPreferences.getInteger(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }


    public boolean contains(String key) {
        return mPreferences.contains(key);
    }


    public KV putString(String key, @Nullable String value) {
        mPreferences.putString(key, value);
        mPreferences.flush();
        return this;
    }

    public KV putInt(String key, int value) {
        mPreferences.putInteger(key, value);
        mPreferences.flush();
        return this;
    }

    public KV putLong(String key, long value) {
        mPreferences.putLong(key, value);
        mPreferences.flush();
        return null;
    }

    public KV putFloat(String key, float value) {
        mPreferences.putFloat(key, value);
        mPreferences.flush();
        return this;
    }

    public KV putBoolean(String key, boolean value) {
        mPreferences.putBoolean(key, value);
        mPreferences.flush();
        return this;
    }

    public KV remove(String key) {
        mPreferences.remove(key);
        mPreferences.flush();
        return this;
    }

    public KV clear() {
        mPreferences.clear();
        mPreferences.flush();
        return this;
    }

    public <T> T getObject(String key, Class<T> t) {
        String str = getString(key, null);
        if (!TextUtils.isEmpty(str)) {
            return mGson.fromJson(str, t);
        } else return null;
    }

    public KV putObject(String key, Object object) {
        String jsonString = mGson.toJson(object);
        KV kv = putString(key, jsonString);
        mPreferences.flush();
        return kv;
    }

}
