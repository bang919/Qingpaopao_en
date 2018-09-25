package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.NewBlogReq;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BuildBlogModel {

    public Observable<NormalRsp> newBlog(String title, String content) {
        NewBlogReq newBlogReq = new NewBlogReq();
        newBlogReq.setTitle(title);
        newBlogReq.setContent(content);
        return HttpClient.getApiInterface().newBlog(newBlogReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
