package com.wopin.qingpaopao.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.common.BaseDialogFragment;
import com.wopin.qingpaopao.presenter.ForgetPasswordPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.ForgetPasswordView;

import java.util.Timer;
import java.util.TimerTask;

public class ForgetPasswordFragment extends BaseDialogFragment<ForgetPasswordPresenter> implements View.OnClickListener, ForgetPasswordView {

    public static final String TAG = "ForgetPasswordFragment";

    private EditText mPhoneNumberEt;
    private EditText mVerificationCodeEt;
    private EditText mPasswordEt;
    private EditText mDoubleCheckPasswordEt;
    private TextView mVerificationCodeView;

    private Timer timer;
    private TimerTask task;
    private int timeSecond = 60;

    @Override
    protected int getLayout() {
        return R.layout.fragment_forget_password;
    }

    @Override
    protected ForgetPasswordPresenter initPresenter() {
        return new ForgetPasswordPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mPhoneNumberEt = rootView.findViewById(R.id.et_phone_number);
        mVerificationCodeEt = rootView.findViewById(R.id.et_verification_code);
        mPasswordEt = rootView.findViewById(R.id.et_password);
        mDoubleCheckPasswordEt = rootView.findViewById(R.id.et_password_doubleinput);
        mVerificationCodeView = rootView.findViewById(R.id.get_verification_code);
        mVerificationCodeView.setOnClickListener(this);

        rootView.findViewById(R.id.iv_back).setOnClickListener(this);
        rootView.findViewById(R.id.bt_confirm).setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }

    @Override
    protected void initEvent() {

    }

    private void startTimer() {
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

    private void cancelTimer() {
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
                dismiss();
                break;
            case R.id.get_verification_code:
                mPresenter.sendVerifyCode(mPhoneNumberEt.getText().toString());
                break;
            case R.id.bt_confirm:
                mPresenter.changePassword(mPhoneNumberEt.getText().toString(), mPasswordEt.getText().toString()
                        , mDoubleCheckPasswordEt.getText().toString(), mVerificationCodeEt.getText().toString());
                break;
        }
    }

    @Override
    public void onSendVerifyCodeComplete() {
        ToastUtils.showShort(R.string.verification_code_had_send);
        startTimer();
    }

    @Override
    public void onChangePasswordSuccess() {
        ToastUtils.showShort(R.string.password_had_be_change);
        dismiss();
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}
