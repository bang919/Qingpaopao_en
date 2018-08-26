package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.request.LoginReq;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.HttpClient;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginModel {

    public Observable<NormalRsp> sendVerifyCode(String phoneNumber) {
        LoginReq loginReq = new LoginReq();
        loginReq.setPhone(phoneNumber);
        return HttpClient.getApiInterface().sendVerifyCode(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> register(LoginReq loginReq) {
        return HttpClient.getApiInterface().register(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> changePassword(LoginReq loginReq) {
        return HttpClient.getApiInterface().changePassword(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginRsp> login(LoginReq loginReq) {
        return HttpClient.getApiInterface().login(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginRsp> loginByThird(String platformName, String userName, String userId, String iconUrl) {
        final ThirdReq thirdReq = new ThirdReq();
        thirdReq.setKey(userId);
        thirdReq.setUserName(userName);
        thirdReq.setType(platformName);
        thirdReq.setIcon(iconUrl);


        return HttpClient.getApiInterface()
                .thirdLogin(thirdReq)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<NormalRsp>>() {
                    @Override
                    public ObservableSource<NormalRsp> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<NormalRsp>>() {
                            @Override
                            public ObservableSource<NormalRsp> apply(Throwable throwable) throws Exception {
                                return HttpClient.getApiInterface().thirdRegister(thirdReq);
                            }
                        });
                    }
                })
                .map(new Function<LoginRsp, LoginRsp>() {
                    @Override
                    public LoginRsp apply(LoginRsp loginRsp) throws Exception {
                        LoginRsp.ResultBean result = loginRsp.getResult();
                        if (result.getIcon() == null || !result.getIcon().equals(thirdReq.getIcon())) {
                            HttpClient.getApiInterface().changeIcon(thirdReq).subscribe();
                        }
                        result.setIcon(thirdReq.getIcon());
                        loginRsp.setResult(result);
                        return loginRsp;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
