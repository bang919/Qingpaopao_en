package com.wopin.qingpaopao.fragment.user_guide;

import android.view.View;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.StringUtils;

import java.util.Locale;

public class HelpCenterFragment extends BaseBarDialogFragment {

    public static final String TAG = "HelpCenterFragment";
    private TextView mTextView;

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
        mTextView = rootView.findViewById(R.id.textview);
    }

    @Override
    protected void initEvent() {
        String helpText = StringUtils.getAssetsTex(Locale.getDefault().getLanguage().equals("zh") ? "help_qa_zh.txt" : "help_qa.txt", getContext());
        mTextView.setText(helpText);
    }


}
