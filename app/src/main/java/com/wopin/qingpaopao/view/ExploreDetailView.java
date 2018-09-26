package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.CommentRsp;

public interface ExploreDetailView {

    void isAuthorFollowed(boolean isFollow);

    void onComments(CommentRsp commentRsp);

    void onMyBlogDelete();

    void onError(String errorString);
}
