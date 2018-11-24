package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SigninModel {

    public Observable<NormalRsp> signinCup(String uuid, String cupId) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"uuid\":\"" + uuid + "\",\"cupId\":\"" + cupId + "\"}");
        return HttpClient.getApiInterface()
                .signinCup(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
