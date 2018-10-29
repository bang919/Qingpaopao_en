package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.OrderBean;
import com.wopin.qingpaopao.bean.response.OrderListResponse;

public interface MyOrderView {

    void onScoresOrder(OrderListResponse scoreOrder);

    void onExchangeOrder(OrderListResponse exchangeOrder);

    void onCrowdfundingOrder(OrderListResponse crowdfundingOrder);

    void onDataResponseSuccess();

    void onDataRefresh();

    void onCrowdfundingOrderPaySuccess();

    void onOldChangeNewOrderPaySuccess(OrderBean orderBean);

    void onError(String errorMessage);
}
