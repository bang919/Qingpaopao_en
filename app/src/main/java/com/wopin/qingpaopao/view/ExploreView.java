package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.ExploreListRsp;

public interface ExploreView {
    void onLoading();

    void onExploreList(ExploreListRsp exploreListRsp);

    void onError(String errorMsg);
}
