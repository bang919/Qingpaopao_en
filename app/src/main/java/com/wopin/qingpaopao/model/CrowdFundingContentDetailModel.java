package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.PaymentBean;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CrowdFundingContentDetailModel {

    public Observable<NormalRsp> payMentCrowdfunding(PaymentBean paymentBean) {
        return HttpClient.getApiInterface().payMentCrowdfunding(paymentBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
