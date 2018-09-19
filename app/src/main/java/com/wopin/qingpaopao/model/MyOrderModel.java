package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.OrderResponse;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyOrderModel {

    public Observable<OrderResponse> getScoresOrder() {
        return HttpClient.getApiInterface().getScoresOrder()
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OrderResponse> getExchangeOrder() {
        return HttpClient.getApiInterface().getExchangeOrder()
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OrderResponse> getCrowdfundingOrder() {
        return HttpClient.getApiInterface().getCrowdfundingOrder()
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
