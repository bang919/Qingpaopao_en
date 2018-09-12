package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.ProductBanner;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.widget.WelfareView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WelfarePresenter extends BasePresenter<WelfareView> {

    public WelfarePresenter(Context context, WelfareView view) {
        super(context, view);
    }

    public void getScoreMarketProduct() {
        mView.onLoading();
        subscribeNetworkTask(getClass().getSimpleName().concat("getScoreMarketProduct"),
                Observable.zip(
                        HttpClient.getApiInterface().getProductBanner("16")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext(new Consumer<ProductBanner>() {
                                    @Override
                                    public void accept(ProductBanner productBanner) throws Exception {
                                        mView.onProductBanner(productBanner);
                                    }
                                }),
                        HttpClient.getApiInterface().getProductContent("16")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext(new Consumer<ArrayList<ProductContent>>() {
                                    @Override
                                    public void accept(ArrayList<ProductContent> productContents) throws Exception {
                                        mView.onProductContentList(productContents);
                                    }
                                }),
                        new BiFunction<ProductBanner, ArrayList<ProductContent>, String>() {
                            @Override
                            public String apply(ProductBanner productBanner, ArrayList<ProductContent> productContents) throws Exception {
                                return "Success";
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                new MyObserver<String>() {
                    @Override
                    public void onMyNext(String s) {
                        mView.onRequestSuccess();
                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });
    }
}
