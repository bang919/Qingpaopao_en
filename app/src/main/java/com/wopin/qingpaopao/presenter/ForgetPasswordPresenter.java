package com.wopin.qingpaopao.presenter;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.view.ForgetPasswordView;
import com.wopin.qingpaopao.view.SendVerifyCodeView;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordView> {

    private LoginModel mLoginModel;
    private SendVerifyCodePresenter mSendVerifyCodePresenter;

    public ForgetPasswordPresenter(Context context, ForgetPasswordView view) {
        super(context, view);
        mLoginModel = new LoginModel();
        mSendVerifyCodePresenter = new SendVerifyCodePresenter(context, new SendVerifyCodeView() {
            @Override
            public void onError(String errorMsg) {
                mView.onError(errorMsg);
            }
        });
    }

    @Override
    public void destroy() {
        mSendVerifyCodePresenter.destroy();
        super.destroy();
    }

    public void setViews(EditText phoneNumberEt, TextView vCodeTv) {
        mSendVerifyCodePresenter.setViews(phoneNumberEt, vCodeTv);
    }

    public void sendVerifyCode() {
        mSendVerifyCodePresenter.sendVerifyCode();
    }

    public void changePassword(final String email) {

        subscribeNetworkTask(getClass().getName().concat("changePassword"), mLoginModel.resetEmailPassword(email), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                mView.onChangePasswordSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
