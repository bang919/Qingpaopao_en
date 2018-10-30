package com.wopin.qingpaopao.model;

import android.text.TextUtils;

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
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    public Observable<NormalRsp> changePassword(String phoneNumber, String vcode, String newPassword) {
        LoginReq loginReq = new LoginReq();
        loginReq.setPhone(phoneNumber);
        loginReq.setV_code(vcode);
        loginReq.setPassword(newPassword);
        return HttpClient.getApiInterface().changePassword(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> changePhone(String phoneNumber, String vcode) {
        LoginReq loginReq = new LoginReq();
        loginReq.setPhone(phoneNumber);
        loginReq.setV_code(vcode);
        return HttpClient.getApiInterface().changePhone(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginRsp> login(LoginReq loginReq) {
        return HttpClient.getApiInterface().login(loginReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginRsp> loginByThird(final ThirdReq thirdReq) {
        return HttpClient.getApiInterface()
                .thirdLogin(thirdReq)
                .map(new Function<LoginRsp, LoginRsp>() {
                    @Override
                    public LoginRsp apply(LoginRsp loginRsp) throws Exception {
                        int status = Integer.valueOf(loginRsp.getStatus());
                        if (status != 0) {
                            throw new Exception(loginRsp.getMsg());
                        }
                        return loginRsp;
                    }
                })
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
                        if (TextUtils.isEmpty(result.getIcon())) {
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"icon\":\"" + thirdReq.getIcon() + "\"}");
                            HttpClient.getApiInterface().changeIcon(requestBody).subscribe();
                            result.setIcon(thirdReq.getIcon());
                            loginRsp.setResult(result);
                        }
                        return loginRsp;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
