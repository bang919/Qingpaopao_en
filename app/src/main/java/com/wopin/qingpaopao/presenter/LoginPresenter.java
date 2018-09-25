package com.wopin.qingpaopao.presenter;

import android.content.Context;
import android.util.Log;

import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.model.ThirdLoginModel;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.view.LoginView;

import cn.sharesdk.framework.Platform;

public class LoginPresenter extends BasePresenter<LoginView> {

    private LoginModel mLoginModel;
    private ThirdLoginModel mThirdLoginModel;

    public LoginPresenter(Context context, LoginView view) {
        super(context, view);
        mLoginModel = new LoginModel();
        mThirdLoginModel = new ThirdLoginModel();
    }

    public void sendVerifyCode(String phoneNumber) {
        mView.onLoading();
        subscribeNetworkTask(getClass().getName().concat("sendVerifyCode"), mLoginModel.sendVerifyCode(phoneNumber), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                mView.onSendVerifyCodeComplete();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void register(final LoginReq loginReq) {
        mView.onLoading();
        subscribeNetworkTask(getClass().getName().concat("register"), mLoginModel.register(loginReq), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                SPUtils.put(MyApplication.getMyApplicationContext(), Constants.USERNAME, loginReq.getPhone());
                mView.onRegisterSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void login(final LoginReq loginReq) {
        mView.onLoading();
        subscribeNetworkTask(getClass().getName().concat("login"), mLoginModel.login(loginReq), new MyObserver<LoginRsp>() {
            @Override
            public void onMyNext(LoginRsp loginResponseBean) {
                Log.d("bigbang", "login : " + Thread.currentThread());
                updateLoginMessage(loginResponseBean);
                SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.LOGIN_REQUEST, loginReq);
                SPUtils.put(MyApplication.getMyApplicationContext(), Constants.USERNAME, loginReq.getPhone());
                mView.onLoginSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void loginByThird(final Platform platform) {
        mView.onLoading();
        mThirdLoginModel.loginByThird(platform, new ThirdLoginModel.ThirdLoginCallback() {
            @Override
            public void onThirdSuccess(String platformName, String userName, String userId, String userIcon) {
                loginByThird(platformName, userName, userId, userIcon);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void loginByThird(String platformName, String userName, String userId, String userIcon) {
        mView.onLoading();
        String observerTag = getClass().getName() + "loginByThird";
        ThirdReq thirdReq = new ThirdReq();
        thirdReq.setKey(userId);
        thirdReq.setUserName(userName);
        thirdReq.setType(platformName);
        thirdReq.setIcon(userIcon);
        SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.THIRD_REQUEST, thirdReq);
        subscribeNetworkTask(observerTag, mLoginModel.loginByThird(thirdReq), new MyObserver<LoginRsp>() {
            @Override
            public void onMyNext(LoginRsp loginRsp) {
                updateLoginMessage(loginRsp);
                mView.onLoginSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    /**
     * Save login message
     */
    public static void updateLoginMessage(LoginRsp loginResponseBean) {
        SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN, loginResponseBean);
    }

    /**
     * Get the account's user/token .
     */
    public static LoginRsp getAccountMessage() {
        try {
            return SPUtils.getObject(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logout();//走到这里就已经失败了，所以logout()
        return null;
    }

    /**
     * Logout a RegisterAndLoginResBean account
     */
    public static void logout() {
        SPUtils.remove(MyApplication.getMyApplicationContext(), Constants.LOGIN_REQUEST);
        SPUtils.remove(MyApplication.getMyApplicationContext(), Constants.THIRD_REQUEST);
        SPUtils.remove(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN);
        SPUtils.remove(MyApplication.getMyApplicationContext(), Constants.SIGN_IN_DATA);
    }
}
