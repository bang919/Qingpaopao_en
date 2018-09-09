package com.wopin.qingpaopao.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wopin.qingpaopao.bean.response.WifiConfigToCupRsp;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.http.HttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WifiCupPostModel {

    public Observable<ArrayList<WifiRsp>> getWifiList() {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, "scan:1\n");
        return HttpClient.getApiInterface().getWifiList(body)
                .retryWhen(getRetryWhen())
                .map(new Function<String, ArrayList<WifiRsp>>() {
                    @Override
                    public ArrayList<WifiRsp> apply(String s) throws Exception {
                        String jsonString = "[" + s + "]";
                        Type founderListType = new TypeToken<ArrayList<WifiRsp>>() {
                        }.getType();
                        return new Gson().fromJson(jsonString, founderListType);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WifiConfigToCupRsp> sendWifiConfigToCup(String ssid, String password) {
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, "ssid: " + ssid + "\n" + "password: " + password);
        return HttpClient.getApiInterface().sendWifiConfigToCup(body)
                .retryWhen(getRetryWhen())
                .map(new Function<String, WifiConfigToCupRsp>() {
                    @Override
                    public WifiConfigToCupRsp apply(String s) throws Exception {
                        return new Gson().fromJson(s, WifiConfigToCupRsp.class);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private Function<Observable<Throwable>, ObservableSource<Long>> getRetryWhen() {
        return new Function<Observable<Throwable>, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.zipWith(Observable.range(1, 10), new BiFunction<Throwable, Integer, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable, Integer integer) throws Exception {
                        return integer;
                    }
                }).flatMap(new Function<Integer, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Integer integer) throws Exception {
                        return Observable.timer(2, TimeUnit.SECONDS);
                    }
                });
            }
        };
    }
}
