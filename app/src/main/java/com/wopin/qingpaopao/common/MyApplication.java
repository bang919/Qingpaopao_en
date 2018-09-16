package com.wopin.qingpaopao.common;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getMyApplicationContext() {
        return mApplicationContext;
    }

}
