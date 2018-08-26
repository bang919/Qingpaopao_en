package com.wopin.qingpaopao.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.BasePresenter;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.view.ForgetPasswordView;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordView> {

    private LoginModel mLoginModel;

    public ForgetPasswordPresenter(Context context, ForgetPasswordView view) {
        super(context, view);
        mLoginModel = new LoginModel();
    }

    public void sendVerifyCode(String phoneNumber) {
        subscribeNetworkTask(getClass().getName().concat("sendVerifyCode"), mLoginModel.sendVerifyCode(phoneNumber), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                mView.onSendVerifyCodeComplete();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void changePassword(final String phoneNumber, String newPassword, String passwordCheck, String vcode) {
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(passwordCheck)) {
            mView.onError(mContext.getString(R.string.please_input_password));
            return;
        } else if (!newPassword.equals(passwordCheck)) {
            mView.onError(mContext.getString(R.string.password_double_check_error));
            return;
        }
        LoginReq loginReq = new LoginReq();
        loginReq.setPhone(phoneNumber);
        loginReq.setV_code(vcode);
        loginReq.setPassword(newPassword);
        subscribeNetworkTask(getClass().getName().concat("changePassword"), mLoginModel.changePassword(loginReq), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                SPUtils.put(MyApplication.getMyApplicationContext(), Constants.USERNAME, phoneNumber);
                mView.onChangePasswordSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
