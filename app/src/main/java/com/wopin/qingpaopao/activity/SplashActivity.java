package com.wopin.qingpaopao.activity;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.common.BaseActivity;
import com.wopin.qingpaopao.presenter.SplashPresenter;
import com.wopin.qingpaopao.view.SplashView;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private static final long SPLASH_WAIT_TIME = 2000;

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
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
