package com.wopin.qingpaopao.view;

public interface CrowdFundingDetailView {

    void onCrowdFundingPrice(String targetPrice, String currentPrice, float percent);

    void onCrowdFundingSupportCount(int totalPeople);

    void onDataSuccess();

    void onCrowdFundingPayMentSuccess();

    void onError(String error);
}
