package com.wopin.qingpaopao.fragment.information_edit;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.presenter.SendVerifyCodePresenter;
import com.wopin.qingpaopao.utils.HttpUtil;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.SendVerifyCodeView;

public class BindPhoneNumberFragment extends BaseBarDialogFragment<SendVerifyCodePresenter> implements View.OnClickListener, SendVerifyCodeView {

    public static final String TAG = "BindPhoneNumberFragment";
    private EditText mPhoneNumberEt;
    private EditText mVCodeEt;
    private TextView mVCodeTv;
    private Button mConfirmBt;

    @Override
    protected String setBarTitle() {
        return getString(R.string.edit_phone_number);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_phone_number;
    }

    @Override
    protected SendVerifyCodePresenter initPresenter() {
        return new SendVerifyCodePresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mPhoneNumberEt = rootView.findViewById(R.id.et_input_phone_number);
        mVCodeEt = rootView.findViewById(R.id.et_input_verification_code);
        mVCodeTv = rootView.findViewById(R.id.get_verification_code);
        mConfirmBt = rootView.findViewById(R.id.bt_confirm);
        mPresenter.setViews(mPhoneNumberEt, mVCodeTv);
    }

    @Override
    protected void initEvent() {
        mVCodeTv.setOnClickListener(this);
        mConfirmBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_verification_code:
                LoginRsp accountMessage = LoginPresenter.getAccountMessage();
                if (mPhoneNumberEt.getText().toString().equals(accountMessage.getResult().getPhone())) {
                    ToastUtils.showShort(R.string.new_phone_number_cant_same);
                    return;
                }
                mPresenter.sendVerifyCode();
                break;
            case R.id.bt_confirm:
                bindNewPhoneNumber();
                break;
        }

    }

    private void bindNewPhoneNumber() {
        final LoginRsp accountMessage = LoginPresenter.getAccountMessage();
        LoginModel loginModel = new LoginModel();
        final String phoneNumber = mPhoneNumberEt.getText().toString();
        String vcode = mVCodeEt.getText().toString();
        if (TextUtils.isEmpty(vcode)) {
            ToastUtils.showShort(R.string.please_input_verification_code);
            return;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.showShort(R.string.please_input_phone_number);
            return;
        } else if (phoneNumber.equals(accountMessage.getResult().getPhone())) {
            ToastUtils.showShort(R.string.new_phone_number_cant_same);
            return;
        }
        HttpUtil.subscribeNetworkTask(loginModel.changePhone(phoneNumber, vcode), new BasePresenter.MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {

                accountMessage.getResult().setPhone(phoneNumber);
                LoginPresenter.updateLoginMessage(accountMessage);
                ToastUtils.showShort(R.string.edit_phone_number_success);
                if (!isDestroy) {
                    dismiss();
                }
            }

            @Override
            public void onMyError(String errorMessage) {
                if (!isDestroy) {
                    ToastUtils.showShort(errorMessage);
                }
            }
        });
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}
