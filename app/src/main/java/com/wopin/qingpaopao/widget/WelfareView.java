package com.wopin.qingpaopao.widget;

import com.wopin.qingpaopao.bean.response.ProductBanner;
import com.wopin.qingpaopao.bean.response.ProductContent;

import java.util.ArrayList;

public interface WelfareView {
    void onProductBanner(ProductBanner productBanner);

    void onProductContentList(ArrayList<ProductContent> productContents);

    void onLoading();

    void onRequestSuccess();

    void onError(String error);
}
