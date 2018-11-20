package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.MyCommentsRsp;

public interface MyCommentView {
    void onMyCommentsRsp(int page, MyCommentsRsp myCommentsRsp);

    void onError(String errorMessage);
}
