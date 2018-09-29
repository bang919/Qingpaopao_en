package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.response.OrderOneResponse;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OldChangeNewContentDetailModel {

    public Observable<OrderOneResponse> payMentExchange(PaymentBean paymentBean) {
        return HttpClient.getApiInterface().payMentExchange(paymentBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
