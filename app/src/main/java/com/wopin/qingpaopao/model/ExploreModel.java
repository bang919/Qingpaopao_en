package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ExploreModel {

    public Observable<ExploreListRsp> listExplores() {
        return HttpClient.getApiInterface().listExplores()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
