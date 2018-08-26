package com.wopin.qingpaopao.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.fragment.ForgetPasswordFragment;
import com.wopin.qingpaopao.fragment.LoginViewFragment;
import com.wopin.qingpaopao.fragment.RegisterViewFragment;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.LoginView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView, View.OnClickListener {

    private Fragment currentFragment;
    private LoginViewFragment mLoginViewFragment;
    private RegisterViewFragment mRegisterViewFragment;
    private long doubleClickToExitTime;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_weixin).setOnClickListener(this);
        findViewById(R.id.iv_qq).setOnClickListener(this);
        findViewById(R.id.iv_weibo).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mLoginViewFragment = new LoginViewFragment();
        mLoginViewFragment.setLoginViewFragmentCallback(new LoginViewFragment.LoginViewFragmentCallback() {
            @Override
            public void onForgetPasswordClick() {
                new ForgetPasswordFragment().show(getSupportFragmentManager(), ForgetPasswordFragment.TAG);
            }

            @Override
            public void login(String phoneNumber, String password) {
                mPresenter.login(phoneNumber, password);
            }

            @Override
            public void switchToRegister() {
                switchFragment(mRegisterViewFragment);
            }
        });
        mRegisterViewFragment = new RegisterViewFragment();
        mRegisterViewFragment.setRegisterViewFragmentCallback(new RegisterViewFragment.RegisterViewFragmentCallback() {
            @Override
            public void onBackClick() {
                switchFragment(mLoginViewFragment);
            }

            @Override
            public void onSendVerificationCodeClick(String phoneNumber) {
                mPresenter.sendVerifyCode(phoneNumber);
            }

            @Override
            public void onRegisterClick(LoginReq loginReq) {
                mPresenter.register(loginReq);
            }
        });
        switchFragment(mLoginViewFragment);
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if (!targetFragment.isAdded()) {
            transaction
                    .add(R.id.fragment_layout, targetFragment)
                    .commit();
        } else {
            transaction
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }


    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_weixin:
                mPresenter.loginByThird(ShareSDK.getPlatform(Wechat.NAME));
                break;
            case R.id.iv_qq:
                mPresenter.loginByThird(ShareSDK.getPlatform(QQ.NAME));
                break;
            case R.id.iv_weibo:
                mPresenter.loginByThird(ShareSDK.getPlatform(SinaWeibo.NAME));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof RegisterViewFragment) {
            switchFragment(mLoginViewFragment);
            return;
        }
        if (System.currentTimeMillis() - doubleClickToExitTime < 1000) {
            finish();
        } else {
            ToastUtils.showShort(R.string.double_click_to_exit);
            doubleClickToExitTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onSendVerifyCodeComplete() {
        ToastUtils.showShort(R.string.verification_code_had_send);
        mRegisterViewFragment.startTimer();
    }

    @Override
    public void onRegisterSuccess() {
        ToastUtils.showShort(R.string.register_success);
        mRegisterViewFragment.cancelTimer();
        mLoginViewFragment.resetPhoneNumber();
        switchFragment(mLoginViewFragment);
    }

    @Override
    public void onLoginSuccess() {
        jumpToActivity(MainActivity.class);
        finish();
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}