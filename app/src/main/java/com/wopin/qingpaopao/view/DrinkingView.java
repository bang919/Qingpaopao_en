package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;

import java.util.ArrayList;

public interface DrinkingView {
    void onLoading();

    void onCupList(ArrayList<CupListRsp.CupBean> cupBeanList, CupListRsp.CupBean currentConnectCup);

    void onTodayDrink(DrinkListTodayRsp drinkListTodayRsp);

    void onTotalDrink(DrinkListTotalRsp drinkListTotalRsp);

    void onError(String errorMsg);
}
