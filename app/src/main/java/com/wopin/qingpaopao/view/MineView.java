package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;

public interface MineView {

    void onTodayDrink(DrinkListTodayRsp drinkListTodayRsp);

    void onTotalDrink(DrinkListTotalRsp drinkListTotalRsp);

    void onRefreshUserData();

    void onError(String errorMessage);
}
