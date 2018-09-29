package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.OrderListResponse;

public interface MyOrderView {

    void onScoresOrder(OrderListResponse scoreOrder);

    void onExchangeOrder(OrderListResponse exchangeOrder);

    void onCrowdfundingOrder(OrderListResponse crowdfundingOrder);

    void onDataResponseSuccess();

    void onDataRefresh();

    void onError(String errorMessage);
}
