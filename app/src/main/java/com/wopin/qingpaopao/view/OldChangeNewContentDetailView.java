package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.OrderBean;
import com.wopin.qingpaopao.bean.response.ProductContent;

import java.util.ArrayList;

public interface OldChangeNewContentDetailView {

    void onOldGoodsList(ArrayList<ProductContent> oldGoodsList);

    void onPayMentExchangeSubmit(OrderBean orderBean);

    void onPaySuccess();

    void onError(String errorSting);
}
