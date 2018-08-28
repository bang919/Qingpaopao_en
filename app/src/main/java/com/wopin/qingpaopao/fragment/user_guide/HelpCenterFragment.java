package com.wopin.qingpaopao.fragment.user_guide;

import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class HelpCenterFragment extends BaseBarDialogFragment {

    public static final String TAG = "HelpCenterFragment";

    @Override
    protected String setBarTitle() {
        return getString(R.string.help_center);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_help_center;
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
