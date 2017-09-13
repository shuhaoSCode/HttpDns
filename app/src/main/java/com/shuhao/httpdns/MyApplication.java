package com.shuhao.httpdns;

import android.app.Application;

import com.sina.util.dnscache.dnsutils.OkHttpDnsHelp;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by luke on 2017/9/13.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpUtils.initClient(
                OkHttpDnsHelp.initOkHttp(
                        getApplicationContext(),
                        "app.yyh.com",
                        10000L, 15000L)
                        .build());


    }
}
