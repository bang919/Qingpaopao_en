package com.wopin.qingpaopao.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.utils.SPUtils;
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

    public void changePassword(final String phoneNumber, String newPassword, String passwordCheck, String vcode) {
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(passwordCheck)) {
            mView.onError(mContext.getString(R.string.please_input_password));
            return;
        } else if (!newPassword.equals(passwordCheck)) {
            mView.onError(mContext.getString(R.string.password_double_check_error));
            return;
        }
        subscribeNetworkTask(getClass().getName().concat("changePassword"), mLoginModel.changePassword(phoneNumber, vcode, newPassword), new MyObserver<NormalRsp>() {
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
