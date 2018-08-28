package com.wopin.qingpaopao.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.presenter.ForgetPasswordPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.ForgetPasswordView;

public class ForgetPasswordFragment extends BaseBarDialogFragment<ForgetPasswordPresenter> implements View.OnClickListener, ForgetPasswordView {

    public static final String TAG = "ForgetPasswordFragment";

    private EditText mPhoneNumberEt;
    private EditText mVerificationCodeEt;
    private EditText mPasswordEt;
    private EditText mDoubleCheckPasswordEt;
    private TextView mVerificationCodeView;

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

//        rootView.findViewById(R.id.iv_back).setOnClickListener(this);
        rootView.findViewById(R.id.bt_confirm).setOnClickListener(this);

        mPresenter.setViews(mPhoneNumberEt, mVerificationCodeView);
    }

    @Override
    protected int setRootLayoutBackgroundColor() {
        return getContext().getResources().getColor(R.color.colorWhite);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.get_verification_code:
                mPresenter.sendVerifyCode();
                break;
            case R.id.bt_confirm:
                mPresenter.changePassword(mPhoneNumberEt.getText().toString(), mPasswordEt.getText().toString()
                        , mDoubleCheckPasswordEt.getText().toString(), mVerificationCodeEt.getText().toString());
                break;
        }
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

    @Override
    protected String setBarTitle() {
        return getString(R.string.forget_password);
    }
}
