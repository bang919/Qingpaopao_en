package com.wopin.qingpaopao.fragment.information_edit;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.presenter.SendVerifyCodePresenter;
import com.wopin.qingpaopao.utils.EncryptionUtil;
import com.wopin.qingpaopao.utils.HttpUtil;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.SendVerifyCodeView;

public class EditPasswordFragment extends BaseBarDialogFragment<SendVerifyCodePresenter> implements View.OnClickListener, SendVerifyCodeView {

    public static final String TAG = "EditPasswordFragment";
    private EditText mPhoneNumberEt;
    private EditText mVCodeEt;
    private TextView mVCodeTv;
    private EditText mPasswordEt;
    private EditText mPasswordDoubleCheckEt;

    @Override
    protected String setBarTitle() {
        return null;
    }

    @Override
    protected void onTopRightCornerTextView(TextView textView) {
        textView.setText(R.string.save);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(mPhoneNumberEt.getText().toString(), mPasswordEt.getText().toString(),
                        mPasswordDoubleCheckEt.getText().toString(), mVCodeEt.getText().toString());
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_password;
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
        mPasswordEt = rootView.findViewById(R.id.et_password);
        mPasswordDoubleCheckEt = rootView.findViewById(R.id.et_password_doubleinput);
        mPresenter.setViews(mPhoneNumberEt, mVCodeTv);
    }

    @Override
    protected void initEvent() {
        String phone = LoginPresenter.getAccountMessage().getResult().getPhone();
        if (TextUtils.isEmpty(phone)) {
            phone = getString(R.string.please_bind_phone_number);
        } else {
            mVCodeTv.setOnClickListener(this);
        }
        mPhoneNumberEt.setText(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_verification_code:
                mPresenter.sendVerifyCode();
                break;
        }
    }

    private void changePassword(final String phoneNumber, String newPassword, String passwordCheck, String vcode) {
        try {
            LoginReq loginReq = SPUtils.getObject(getContext(), Constants.LOGIN_REQUEST);
            if (loginReq != null && !TextUtils.isEmpty(newPassword) && EncryptionUtil.md5(newPassword).equals(loginReq.getPassword())) {
                ToastUtils.showShort(R.string.new_password_cant_same);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(passwordCheck)) {
            ToastUtils.showShort(R.string.please_input_password);
            return;
        } else if (!newPassword.equals(passwordCheck)) {
            ToastUtils.showShort(R.string.password_double_check_error);
            return;
        }
        LoginModel loginModel = new LoginModel();
        HttpUtil.subscribeNetworkTask(loginModel.changePassword(phoneNumber, vcode, newPassword), new BasePresenter.MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                ToastUtils.showShort(R.string.password_had_be_change);
                if (!isDestroy) {
                    dismiss();
                }
            }

            @Override
            public void onMyError(String errorMessage) {
                ToastUtils.showShort(errorMessage);
            }
        });
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}
