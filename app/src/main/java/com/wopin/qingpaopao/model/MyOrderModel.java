package com.wopin.qingpaopao.model;

import android.util.Log;

import com.google.gson.Gson;
import com.wopin.qingpaopao.bean.request.TrackingNumberSettingBean;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.OrderBean;
import com.wopin.qingpaopao.bean.response.OrderListResponse;
import com.wopin.qingpaopao.http.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyOrderModel {

    public Observable<OrderListResponse> getScoresOrder() {
        return HttpClient.getApiInterface().getScoresOrder()
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OrderListResponse> getExchangeOrder() {
        return HttpClient.getApiInterface().getExchangeOrder()
                .retry(2)
                .map(new Function<ResponseBody, OrderListResponse>() {
                    @Override
                    public OrderListResponse apply(ResponseBody responseBody) throws Exception {
                        String string = responseBody.string();
                        JSONObject jsonObject = new JSONObject(string);
                        JSONArray results = jsonObject.optJSONArray("result");
                        Gson gson = new Gson();
                        ArrayList<OrderBean> orderBeans = new ArrayList<OrderBean>();
                        for (int i = 0; i < results.length(); i++) {
                            try {
                                OrderBean orderBean = gson.fromJson(results.opt(i).toString(), OrderBean.class);
                                orderBeans.add(orderBean);
                            } catch (Exception e) {
                                Log.d("MyOrderModel", "apply: bad address");
                            }
                        }
                        OrderListResponse orderListResponse = new OrderListResponse();
                        orderListResponse.setOrderBeans(orderBeans);
                        return orderListResponse;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<OrderListResponse> getCrowdfundingOrder() {
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
