package com.wopin.qingpaopao.view;

public interface BuildBlogView {
    void onBlogBuildSuccess();

    void onImageUpdating();

    void onImageGet(String imageUrl);

    void onError(String errorString);
}
