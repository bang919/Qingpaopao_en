package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.CommentRsp;

public interface ExploreDetailView {
    void onComments(CommentRsp commentRsp);

    void onError(String errorString);
}
