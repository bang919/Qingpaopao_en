package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.BlogPostRsp;
import com.wopin.qingpaopao.bean.response.ExploreListRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ExploreModel {

    public Observable<ExploreListRsp> listHotExplores() {
        return HttpClient.getApiInterface().listHotExplores(1, 99)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listNewlyExplores() {
        return HttpClient.getApiInterface().listNewlyExplores(1, 99)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listMyExplores() {
        return HttpClient.getApiInterface().listMyExplores(1, 99)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> searchExplores(String searchString) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"search\":\"" + searchString + "\"}");
        return HttpClient.getApiInterface().searchExplores(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BlogPostRsp> getBlogPost(String exploreid) {
        return HttpClient.getApiInterface().getBlogPost(exploreid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
