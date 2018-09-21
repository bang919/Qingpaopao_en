package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.BodyProfilesBean;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyHealthModel {

    public Observable<LoginRsp> updateBodyProfiles(BodyProfilesBean bodyProfilesBean) {
        return HttpClient.getApiInterface().updateBodyProfiles(bodyProfilesBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
