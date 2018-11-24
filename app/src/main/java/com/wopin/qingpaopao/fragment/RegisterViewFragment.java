package com.wopin.qingpaopao.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.EmailLoginReq;
import com.wopin.qingpaopao.utils.StringUtils;
import com.wopin.qingpaopao.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterViewFragment extends Fragment implements View.OnClickListener {

    private View mRootView;
    private RegisterViewFragmentCallback mRegisterViewFragmentCallback;
    private EditText mPhoneNumberEt;
    private EditText mVerificationCodeEt;
    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private TextView mVerificationCodeView;

    private Timer timer;
    private TimerTask task;
    private int timeSecond = 60;

    public void setRegisterViewFragmentCallback(RegisterViewFragmentCallback registerViewFragmentCallback) {
        mRegisterViewFragmentCallback = registerViewFragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(), container, false);
        onViewPagerFragmentCreate();

        return mRootView;
    }

    @Override
    public void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }

    private int getLayout() {
        return R.layout.view_register;
    }

    private void onViewPagerFragmentCreate() {
        mPhoneNumberEt = mRootView.findViewById(R.id.et_phone_number);
        mVerificationCodeEt = mRootView.findViewById(R.id.et_verification_code);
        mUsernameEt = mRootView.findViewById(R.id.et_input_username);
        mPasswordEt = mRootView.findViewById(R.id.et_password);
        mVerificationCodeView = mRootView.findViewById(R.id.get_verification_code);
        mVerificationCodeView.setOnClickListener(this);

        mRootView.findViewById(R.id.iv_back).setOnClickListener(this);
        mRootView.findViewById(R.id.bt_register).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_user_agreement).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_user_agreement).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_privacy_policy).setOnClickListener(this);


    }

    public void startTimer() {
        cancelTimer();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (timeSecond == 1) {
                    cancelTimer();
                } else {
                    mVerificationCodeView.post(new Runnable() {
                        @Override
                        public void run() {
                            mVerificationCodeView.setText(getString(R.string.get_verification_code).concat("(").concat(String.valueOf(timeSecond).concat(")")));
                        }
                    });
                    timeSecond--;
                }
            }
        };
        timer.schedule(task, 0, 1000);
        mVerificationCodeView.setClickable(false);
    }

    public void cancelTimer() {
        mVerificationCodeView.post(new Runnable() {
            @Override
            public void run() {
                mVerificationCodeView.setText(R.string.get_verification_code);
            }
        });
        timeSecond = 60;
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        mVerificationCodeView.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mRegisterViewFragmentCallback != null) {
                    mRegisterViewFragmentCallback.onBackClick();
                }
                break;
            case R.id.bt_register:
                String email = mPhoneNumberEt.getText().toString();
                if (!StringUtils.isEmail(email)) {
                    ToastUtils.showShort(R.string.email_format_error);
                    return;
                }
                if (mRegisterViewFragmentCallback != null) {
                    EmailLoginReq emailLoginReq = new EmailLoginReq();
                    emailLoginReq.setEmail(email);
                    emailLoginReq.setUserName(mUsernameEt.getText().toString());
                    emailLoginReq.setPassword(mPasswordEt.getText().toString());
                    mRegisterViewFragmentCallback.onRegisterClick(emailLoginReq);
                }
                break;
            case R.id.get_verification_code:
                if (mRegisterViewFragmentCallback != null) {
                    mRegisterViewFragmentCallback.onSendVerificationCodeClick(mPhoneNumberEt.getText().toString());
                }
                break;
            case R.id.tv_user_agreement:
            case R.id.tv_privacy_policy:
                new UserAgreementFragment().show(getChildFragmentManager(), UserAgreementFragment.TAG);
                break;
        }
    }

    public interface RegisterViewFragmentCallback {
        void onBackClick();

        void onSendVerificationCodeClick(String phoneNumber);

        void onRegisterClick(EmailLoginReq emailLoginReq);
    }
}
