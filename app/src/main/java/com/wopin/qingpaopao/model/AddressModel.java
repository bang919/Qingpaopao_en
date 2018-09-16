package com.wopin.qingpaopao.model;

import android.support.annotation.NonNull;

import com.wopin.qingpaopao.bean.request.AddressBean;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.http.ApiInterface;
import com.wopin.qingpaopao.http.HttpClient;
import com.wopin.qingpaopao.presenter.LoginPresenter;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AddressModel {

    public Observable<LoginRsp> addOrUpdateAddress(AddressBean addressBean) {
        final ApiInterface apiInterface = HttpClient.getApiInterface();
        return apiInterface.addOrUpdateAddress(addressBean)
                .flatMap(getUserData(apiInterface))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginRsp> setDefaultAddress(AddressBean addressBean) {
        final ApiInterface apiInterface = HttpClient.getApiInterface();
        return apiInterface.setDefaultAddress(addressBean)
                .flatMap(getUserData(apiInterface))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private Function<NormalRsp, ObservableSource<LoginRsp>> getUserData(final ApiInterface apiInterface) {
        return new Function<NormalRsp, ObservableSource<LoginRsp>>() {
            @Override
            public ObservableSource<LoginRsp> apply(NormalRsp normalRsp) throws Exception {
                return apiInterface.getUserData().doOnNext(new Consumer<LoginRsp>() {
                    @Override
                    public void accept(LoginRsp loginRsp) throws Exception {
                        LoginPresenter.updateLoginMessage(loginRsp);
                    }
                });
            }
        };
    }
}
