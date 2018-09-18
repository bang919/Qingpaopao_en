package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.ProductContent;

import java.util.ArrayList;

public interface OldChangeNewContentDetailView {

    void onOldGoodsList(ArrayList<ProductContent> oldGoodsList);

    void onPayMentExchangeSubmit();

    void onError(String errorSting);
}
