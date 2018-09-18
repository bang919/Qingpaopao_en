package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.CrowdfundingOrderTotalRsp;
import com.wopin.qingpaopao.bean.response.ProductBanner;
import com.wopin.qingpaopao.bean.response.ProductContent;
import com.wopin.qingpaopao.http.HttpClient;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WelfareModel {


    public Observable<CrowdfundingOrderTotalRsp> crowdfundingOrderTotalMoney(int goodsId) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"goodsId\":\"" + goodsId + "\"}");
        return HttpClient.getApiInterface().crowdfundingOrderTotalMoney(requestBody)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ProductBanner> getProductBanner(int categoryId) {
        return HttpClient.getApiInterface().getProductBanner(String.valueOf(categoryId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<ProductContent>> getProductContent(int categoryId) {
        return HttpClient.getApiInterface().getProductContent(String.valueOf(categoryId))
                .map(new Function<ArrayList<ProductContent>, ArrayList<ProductContent>>() {
                    @Override
                    public ArrayList<ProductContent> apply(ArrayList<ProductContent> productContents) throws Exception {
                        for (ProductContent productContent : productContents) {
                            productContent.initDescriptionImages();
                        }
                        return productContents;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
