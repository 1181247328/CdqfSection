package com.cdqf.cart_hear;

import com.cdqf.exception.CauchExceptionHandler;
import com.cdqf.exception.LogToFile;
import com.mob.MobApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zxy.tiny.Tiny;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by gaolf on 15/12/24.
 */
public class App extends MobApplication {
    private static App sInstance = new App();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Tiny.getInstance().init(this);
        CauchExceptionHandler.getInstance().setDefaultUnCachExceptionHandler();
        LogToFile.init(getApplicationContext());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }

    public static App getInstance() {
        return sInstance;
    }
}
