package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.ScoreMarketPayment;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScoreMarketContentDetailModel {

    public Observable<NormalRsp> payMentScores(ScoreMarketPayment scoreMarketPayment) {
        return HttpClient.getApiInterface().payMentScores(scoreMarketPayment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
