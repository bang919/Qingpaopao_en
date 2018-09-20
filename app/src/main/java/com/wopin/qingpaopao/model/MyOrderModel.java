package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.TrackingNumberSettingBean;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.OrderResponse;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    public Observable<NormalRsp> exchangeOrderUpdate(TrackingNumberSettingBean trackingNumberSettingBean) {
        return HttpClient.getApiInterface().exchangeOrderUpdate(trackingNumberSettingBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> deleteOrder(String orderId) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"orderId\":\"" + orderId + "\"}");
        return HttpClient.getApiInterface().deleteOrder(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
