package com.wopin.qingpaopao.common;

import android.app.Application;
import android.content.Context;
import android.webkit.WebView;

import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.mob.MobSDK;


/**
 * Created by Administrator on 2017/11/7.
 */

public class MyApplication extends Application {

    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        CityListLoader.getInstance().loadProData(this);
        mApplicationContext = getApplicationContext();
        initWebView();
    }

    //解决Android N之后WebView导致多语言失效问题
    private void initWebView() {
        new WebView(this).destroy();
    }

    public static Context getMyApplicationContext() {
        return mApplicationContext;
    }

}
