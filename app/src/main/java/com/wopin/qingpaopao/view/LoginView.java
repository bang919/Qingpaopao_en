package com.wopin.qingpaopao.view;

public interface LoginView {
    void onLoading();

    void onSendVerifyCodeComplete();

    void onRegisterSuccess();

    void onLoginSuccess();

    void onError(String errorMsg);
}
