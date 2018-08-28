package com.wopin.qingpaopao.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.SendVerifyCodeView;

import java.util.Timer;
import java.util.TimerTask;

public class SendVerifyCodePresenter<V extends SendVerifyCodeView> extends BasePresenter<V> {

    private LoginModel mLoginModel;
    private TextView mVCodeTv;
    private EditText mPhoneNumberEt;
    private Timer timer;
    private TimerTask task;
    private int timeSecond = 60;

    public <M extends V> SendVerifyCodePresenter(Context context, M view) {
        super(context, view);
        mLoginModel = new LoginModel();
    }

    public void setViews(EditText phoneNumberEt, TextView vCodeTv) {
        mPhoneNumberEt = phoneNumberEt;
        mVCodeTv = vCodeTv;
    }

    @Override
    public void destroy() {
        cancelTimer();
        super.destroy();
    }

    private void startTimer() {
        if (mVCodeTv == null) {
            throw new RuntimeException("Have not setViews");
        }
        cancelTimer();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (timeSecond == 1) {
                    cancelTimer();
                } else {
                    mVCodeTv.post(new Runnable() {
                        @Override
                        public void run() {
                            mVCodeTv.setText(mVCodeTv.getContext().getString(R.string.get_verification_code).concat("(").concat(String.valueOf(timeSecond).concat(")")));
                        }
                    });
                    timeSecond--;
                }
            }
        };
        timer.schedule(task, 0, 1000);
        mVCodeTv.setClickable(false);
    }

    private void cancelTimer() {
        if (mVCodeTv == null) {
            throw new RuntimeException("Have not setViews");
        }
        mVCodeTv.post(new Runnable() {
            @Override
            public void run() {
                mVCodeTv.setText(R.string.get_verification_code);
            }
        });
        timeSecond = 60;
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        mVCodeTv.setClickable(true);
    }

    public void sendVerifyCode() {
        if (mPhoneNumberEt == null) {
            throw new RuntimeException("Have not setViews");
        }
        String phoneNumber = mPhoneNumberEt.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mView.onError(mContext.getString(R.string.please_input_phone_number));
            return;
        }
        subscribeNetworkTask(getClass().getSimpleName().concat("sendVerifyCode"), mLoginModel.sendVerifyCode(phoneNumber), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                startTimer();
                ToastUtils.showShort(R.string.verification_code_had_send);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
