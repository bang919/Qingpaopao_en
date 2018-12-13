package com.wopin.qingpaopao.activity;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.presenter.SplashPresenter;
import com.wopin.qingpaopao.utils.ChangeLanguageHelper;
import com.wopin.qingpaopao.view.SplashView;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private static final long SPLASH_WAIT_TIME = 2000;

    @Override
    protected int getLayout() {
        setLanguage();
        return R.layout.activity_splash;
    }

    private void setLanguage() {
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

    @Override
    protected void onDestroy() {
        getWindow().setBackgroundDrawable(null);//theme里面设置了windowBackground，需要清理
        super.onDestroy();
    }
}
