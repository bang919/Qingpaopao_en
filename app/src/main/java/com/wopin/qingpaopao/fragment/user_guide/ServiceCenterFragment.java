package com.wopin.qingpaopao.fragment.user_guide;

import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class ServiceCenterFragment extends BaseBarDialogFragment {

    public static final String TAG = "ServiceCenterFragment";

    @Override
    protected String setBarTitle() {
        return getString(R.string.service_center);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_service_center;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {

    }
}
