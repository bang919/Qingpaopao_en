package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.OrderResponse;

public interface MyOrderView {

    void onScoresOrder(OrderResponse scoreOrder);

    void onExchangeOrder(OrderResponse exchangeOrder);

    void onCrowdfundingOrder(OrderResponse crowdfundingOrder);

    void onDataResponseSuccess();

    void onError(String errorMessage);
}
