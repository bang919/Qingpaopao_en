package com.wopin.qingpaopao.fragment;

import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class UserAgreementFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String TAG = "UserAgreementFragment";

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_agreement;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.bt_close).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_close:
                dismiss();
                break;
        }
    }
}
