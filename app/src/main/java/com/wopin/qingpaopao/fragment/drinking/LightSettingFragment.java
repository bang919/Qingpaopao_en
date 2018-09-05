package com.wopin.qingpaopao.fragment.drinking;

import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class LightSettingFragment extends BaseDialogFragment implements View.OnClickListener {

    public static final String TAG = "LightSettingFragment";

    @Override
    protected int getLayout() {
        return R.layout.fragment_light_setting;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.iv_back).setOnClickListener(this);
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
        }
    }
}
