package com.wopin.qingpaopao.fragment.system_setting;

import android.view.View;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class AboutUsFragment extends BaseBarDialogFragment {

    public static final String TAG = "AboutUsFragment";

    @Override
    protected String setBarTitle() {
        return getString(R.string.about_us);
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
        TextView textView = rootView.findViewById(R.id.textview);
        textView.setText(R.string.app_synopsis);
    }

    @Override
    protected void initEvent() {

    }
}
