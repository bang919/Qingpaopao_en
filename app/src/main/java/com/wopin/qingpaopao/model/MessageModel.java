package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.CheckNewMessageRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageModel {

    public Observable<CheckNewMessageRsp> checkNewMessage() {
        return HttpClient.getApiInterface().checkNewMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
