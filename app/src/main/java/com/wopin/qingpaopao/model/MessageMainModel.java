package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageMainModel {

    public Observable<SystemMessageRsp> getSystemMessage() {
        return HttpClient.getApiInterface().getSystemMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NewCommentRsp> getNewCommentNotify() {
        return HttpClient.getApiInterface()
                .getNewBlogMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
