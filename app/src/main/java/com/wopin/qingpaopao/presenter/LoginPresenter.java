package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.BasePresenter;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.model.LoginModel;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.view.LoginView;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginPresenter extends BasePresenter<LoginView> {

    private LoginModel mLoginModel;

    public LoginPresenter(Context context, LoginView view) {
        super(context, view);
        mLoginModel = new LoginModel();
    }

    public void sendVerifyCode(String phoneNumber) {
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

    public void login(final String phoneNumber, String password) {
        LoginReq loginReq = new LoginReq();
        loginReq.setPhone(phoneNumber);
        loginReq.setPassword(password);
        subscribeNetworkTask(getClass().getName().concat("login"), mLoginModel.login(loginReq), new MyObserver<LoginRsp>() {
            @Override
            public void onMyNext(LoginRsp loginResponseBean) {
                updateLoginMessage(loginResponseBean);
                SPUtils.put(MyApplication.getMyApplicationContext(), Constants.USERNAME, phoneNumber);
                mView.onLoginSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void loginByThird(final Platform platform) {
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
        platform.SSOSetting(false);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                PlatformDb db = platform.getDb();
                String platformName = db.getPlatformNname();
                String userName = db.getUserName();
                String userId = db.getUserId();
                String userIcon = db.getUserIcon();
                loginByThird(platformName, userName, userId, userIcon);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                String errorMsg = throwable.toString();
                if (errorMsg.contains("WechatClientNotExistException")) {
                    errorMsg = mContext.getString(R.string.wechat_client_not_exist);
                }
                Disposable subscribe = Observable.just(errorMsg)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                mView.onError(s);
                            }
                        });
            }

            @Override
            public void onCancel(Platform platform, int i) {
                mView.onError(mContext.getString(R.string.cancel));
            }
        });
        platform.showUser(null);
    }

    private void loginByThird(String platformName, String userName, String userId, String userIcon) {
        final String observerTag = getClass().getName() + "loginByThird";
        subscribeNetworkTask(observerTag, mLoginModel.loginByThird(platformName, userName, userId, userIcon), new MyObserver<LoginRsp>() {
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
        SPUtils.remove(MyApplication.getMyApplicationContext(), Constants.LOGIN_BEAN);
    }
}
