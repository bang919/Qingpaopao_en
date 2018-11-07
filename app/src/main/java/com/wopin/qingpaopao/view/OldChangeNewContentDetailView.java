package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.OrderBean;

public interface OldChangeNewContentDetailView {

    void onPayMentExchangeSubmit(OrderBean orderBean);

    void onPaySuccess(OrderBean orderBean);

    void onOrderUpdateSuccess();

    void onError(String errorSting);
}
