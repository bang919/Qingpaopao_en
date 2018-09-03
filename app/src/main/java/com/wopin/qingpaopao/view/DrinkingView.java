package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.CupListRsp;

import java.util.ArrayList;

public interface DrinkingView {
    void onCupList(ArrayList<CupListRsp.CupBean> cupBeanList, CupListRsp.CupBean currentConnectCup);

    void onError(String errorMsg);
}
