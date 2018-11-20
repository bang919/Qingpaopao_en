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

    public Observable<ExploreListRsp> listHotExplores(int page, int number) {//热门话题
        return HttpClient.getApiInterface().listHotExplores(page, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listNewlyExplores(int page, int number) {//最新话题
        return HttpClient.getApiInterface().listNewlyExplores(page, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listMyExplores(int page, int number) {//我的话题
        return HttpClient.getApiInterface().listMyExplores(page, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listFollowExplores(int page, int number) {//关注话题
        return HttpClient.getApiInterface().listFollowExplores(page, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listColletionExplores(int page, int number) {//收藏话题
        return HttpClient.getApiInterface().listColletionExplores(page, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listHistoryExplores(int page, int number) {//浏览历史
        return HttpClient.getApiInterface().listHistoryExplores(page, number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ExploreListRsp> listMyLikeExplores(int page, int number) {//浏览历史
        return HttpClient.getApiInterface().listMyLikeExplores(page, number)
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
