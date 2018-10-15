package com.wopin.qingpaopao.activity;

import android.support.v4.app.Fragment;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MyFragmentPageAdapter;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.fragment.ForgetPasswordFragment;
import com.wopin.qingpaopao.fragment.LoginViewFragment;
import com.wopin.qingpaopao.fragment.RegisterViewFragment;
import com.wopin.qingpaopao.m_richeditor.keyboard.KeyboardUtils;
import com.wopin.qingpaopao.presenter.LoginPresenter;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.LoginView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView, View.OnClickListener {

    private LoginViewFragment mLoginViewFragment;
    private RegisterViewFragment mRegisterViewFragment;
    private long doubleClickToExitTime;
    private MyFragmentPageAdapter mMyFragmentPageAdapter;

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

    private void setLoadingVisibility(boolean isVisibility) {
        findViewById(R.id.progress_bar_layout).setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        KeyboardUtils.hideSoftInput(LoginActivity.this);
        super.onDestroy();
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
                LoginReq loginReq = new LoginReq();
                loginReq.setPhone(phoneNumber);
                loginReq.setPassword(password);
                mPresenter.login(loginReq);
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
                onBackPressed();
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
        if (mMyFragmentPageAdapter == null) {
            mMyFragmentPageAdapter = new MyFragmentPageAdapter(this, R.id.fragment_layout);
        }
        mMyFragmentPageAdapter.switchToFragment(targetFragment, true);
    }


    @Override
    protected void initEvent() {
        try {
            LoginReq loginReq = SPUtils.getObject(this, Constants.LOGIN_REQUEST);
            ThirdReq thirdReq = SPUtils.getObject(this, Constants.THIRD_REQUEST);
            if (loginReq != null) {
                mPresenter.login(loginReq);
            } else if (thirdReq != null) {
                mPresenter.loginByThird(thirdReq.getType(), thirdReq.getUserName(), thirdReq.getKey(), thirdReq.getIcon());
            }
        } catch (Exception e) {
            LoginPresenter.logout();
            e.printStackTrace();
        }
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
        if (System.currentTimeMillis() - doubleClickToExitTime < 1000) {
            finish();
        } else {
            ToastUtils.showShort(R.string.double_click_to_exit);
            doubleClickToExitTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onLoading() {
        setLoadingVisibility(true);
    }

    @Override
    public void onSendVerifyCodeComplete() {
        setLoadingVisibility(false);
        ToastUtils.showShort(R.string.verification_code_had_send);
        mRegisterViewFragment.startTimer();
    }

    @Override
    public void onRegisterSuccess() {
        setLoadingVisibility(false);
        ToastUtils.showShort(R.string.register_success);
        mRegisterViewFragment.cancelTimer();
        mLoginViewFragment.resetPhoneNumber();
        switchFragment(mLoginViewFragment);
    }

    @Override
    public void onLoginSuccess() {
        setLoadingVisibility(false);
        jumpToActivity(MainActivity.class);
        finish();
    }

    @Override
    public void onError(String errorMsg) {
        setLoadingVisibility(false);
        ToastUtils.showShort(errorMsg);
    }
}
