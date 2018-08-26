package com.wopin.qingpaopao.view;

public interface ForgetPasswordView {
    void onSendVerifyCodeComplete();

    void onChangePasswordSuccess();

    void onError(String errorMsg);
}
