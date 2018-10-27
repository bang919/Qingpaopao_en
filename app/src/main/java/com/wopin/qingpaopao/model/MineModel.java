package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.presenter.LoginPresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MineModel {

    public Observable<LoginRsp> getUserData() {
        return HttpClient.getApiInterface().getUserData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<LoginRsp>() {
                    @Override
                    public void accept(LoginRsp loginRsp) throws Exception {
                        LoginPresenter.updateLoginMessage(loginRsp);
                    }
                });
    }
}
