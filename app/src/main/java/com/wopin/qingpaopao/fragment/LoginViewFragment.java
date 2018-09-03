package com.wopin.qingpaopao.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.utils.SPUtils;

public class LoginViewFragment extends Fragment implements View.OnClickListener {

    private View mRootView;
    private LoginViewFragmentCallback mLoginViewFragmentCallback;

    private EditText mPhoneNumberEt;
    private EditText mPasswordEt;

    public void setLoginViewFragmentCallback(LoginViewFragmentCallback loginViewFragmentCallback) {
        mLoginViewFragmentCallback = loginViewFragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(), container, false);
        onViewPagerFragmentCreate();
        resetPhoneNumber();
        return mRootView;
    }

    private int getLayout() {
        return R.layout.view_login;
    }

    private void onViewPagerFragmentCreate() {
        mPhoneNumberEt = mRootView.findViewById(R.id.et_phone_number);
        mPasswordEt = mRootView.findViewById(R.id.et_password);

        mRootView.findViewById(R.id.tv_forgetpassword).setOnClickListener(this);
        mRootView.findViewById(R.id.bt_login).setOnClickListener(this);
        mRootView.findViewById(R.id.go_to_register).setOnClickListener(this);
    }

    public void resetPhoneNumber() {
        String oldPhoneNumber = (String) SPUtils.get(getContext(), Constants.USERNAME, "");
        if (!TextUtils.isEmpty(oldPhoneNumber)) {
            mPhoneNumberEt.setText(oldPhoneNumber);
            mPasswordEt.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgetpassword:
                if (mLoginViewFragmentCallback != null) {
                    mLoginViewFragmentCallback.onForgetPasswordClick();
                }
                break;
            case R.id.bt_login:
                if (mLoginViewFragmentCallback != null) {
                    mLoginViewFragmentCallback.login(mPhoneNumberEt.getText().toString(), mPasswordEt.getText().toString());
                }
                break;
            case R.id.go_to_register:
                if (mLoginViewFragmentCallback != null) {
                    mLoginViewFragmentCallback.switchToRegister();
                }
                break;
        }
    }

    public interface LoginViewFragmentCallback {
        void onForgetPasswordClick();

        void login(String phoneNumber, String password);

        void switchToRegister();
    }
}
