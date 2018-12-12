package com.wopin.qingpaopao.activity;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.presenter.SplashPresenter;
import com.wopin.qingpaopao.utils.ChangeLanguageHelper;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.view.SplashView;

import java.util.Locale;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private static final long SPLASH_WAIT_TIME = 2000;

    @Override
    protected int getLayout() {
        setLanguage();
        return R.layout.activity_splash;
    }

    private void setLanguage() {

        if ((int) SPUtils.get(this, Constants.DEFAULT_LANGUAGE, ChangeLanguageHelper.CHANGE_LANGUAGE_DEFAULT) == ChangeLanguageHelper.CHANGE_LANGUAGE_DEFAULT) {
            SPUtils.put(this, Constants.DEFAULT_LANGUAGE, Locale.getDefault().getLanguage().equals("zh") ? ChangeLanguageHelper.CHANGE_LANGUAGE_CHINA : ChangeLanguageHelper.CHANGE_LANGUAGE_ENGLISH);
        }

        int appLanguage = ChangeLanguageHelper.getAppLanguage();
        ChangeLanguageHelper.changeLanguage(getResources(), appLanguage);
    }

    @Override
    protected SplashPresenter initPresenter() {
        return new SplashPresenter(this, this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mPresenter.jumpActivityDelay(SPLASH_WAIT_TIME);
    }

    @Override
    public void delayToActivity() {
        jumpToActivity(LoginActivity.class);
        finish();
    }
}
