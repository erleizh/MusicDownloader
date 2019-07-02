package com.erlei.musicdownloader.base;


/**
 * Created by lll on 18-3-24.
 * Email : lllemail@foxmail.com
 * Describe : 全局配置
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Config {

    public static final String BASE_URL = "http://139.199.115.248:2345/";

    public static final String TEST_URL = "http://139.199.115.248:2346/";

    public static final String URL;


    static {
        URL = BASE_URL;
//        URL = TEST_URL;
    }
}
