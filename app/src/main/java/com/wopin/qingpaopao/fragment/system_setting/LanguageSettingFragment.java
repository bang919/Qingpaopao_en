package com.wopin.qingpaopao.fragment.system_setting;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.activity.MainActivity;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ChangeLanguageHelper;

public class LanguageSettingFragment extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "LanguageSettingFragment";

    private CheckBox mEnglishBox;
    private CheckBox mChineseBox;
    private int language = ChangeLanguageHelper.getAppLanguage();

    @Override
    protected void onTopRightCornerTextView(TextView textView) {
        textView.setText(R.string.save);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeLanguageHelper.changeLanguage(getResources(), language);

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.language_setting);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_language_setting;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mEnglishBox = rootView.findViewById(R.id.english_checkbox);
        mChineseBox = rootView.findViewById(R.id.chinese_checkbox);

        mEnglishBox.setOnClickListener(this);
        mChineseBox.setOnClickListener(this);

        rootView.findViewById(R.id.english_tv).setOnClickListener(this);
        rootView.findViewById(R.id.chinese_tv).setOnClickListener(this);

        if (language == ChangeLanguageHelper.CHANGE_LANGUAGE_ENGLISH) {
            mEnglishBox.setChecked(true);
        } else if (language == ChangeLanguageHelper.CHANGE_LANGUAGE_CHINA) {
            mChineseBox.setChecked(true);
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.english_tv:
            case R.id.english_checkbox:
                mEnglishBox.setChecked(true);
                mChineseBox.setChecked(false);
                language = ChangeLanguageHelper.CHANGE_LANGUAGE_ENGLISH;
                break;
            case R.id.chinese_tv:
            case R.id.chinese_checkbox:
                mEnglishBox.setChecked(false);
                mChineseBox.setChecked(true);
                language = ChangeLanguageHelper.CHANGE_LANGUAGE_CHINA;
                break;
        }
    }
}
