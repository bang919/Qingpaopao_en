package com.wopin.qingpaopao.fragment.drinking;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.SigninPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.SigninView;

public class ActivateFragment extends BaseBarDialogFragment<SigninPresenter> implements View.OnClickListener, SigninView {

    public static final String TAG = "ActivateFragment";
    private EditText mActivateEt;
    private ActivateCallback mActivateCallback;

    private void setActivateCallback(ActivateCallback activateCallback) {
        mActivateCallback = activateCallback;
    }

    public static ActivateFragment build(String cupId, ActivateCallback activateCallback) {
        ActivateFragment activateFragment = new ActivateFragment();
        Bundle args = new Bundle();
        args.putString(TAG, cupId);
        activateFragment.setArguments(args);
        activateFragment.setActivateCallback(activateCallback);
        return activateFragment;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.activate_cup);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_activate;
    }

    @Override
    protected SigninPresenter initPresenter() {
        return new SigninPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mActivateEt = rootView.findViewById(R.id.et_activate);
        rootView.findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                String s = mActivateEt.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastUtils.showShort(R.string.activate_text);
                    return;
                }
                String cupId = getArguments().getString(TAG);
                mPresenter.signinCup(s, cupId);
                setLoadingVisibility(true);
                break;
        }
    }

    @Override
    public void onSigninSuccess() {
        setLoadingVisibility(false);
        ToastUtils.showShort(R.string.activate_success);
        dismiss();
        if (mActivateCallback != null) {
            mActivateCallback.onSigninSuccess();
        }
    }

    @Override
    public void onError(String errorMsg) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMsg);
    }

    public interface ActivateCallback {
        void onSigninSuccess();
    }
}
