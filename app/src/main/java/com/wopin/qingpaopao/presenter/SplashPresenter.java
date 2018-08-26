package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.common.BasePresenter;
import com.wopin.qingpaopao.view.SplashView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter extends BasePresenter<SplashView> {


    public SplashPresenter(Context context, SplashView view) {
        super(context, view);
    }

    public void jumpActivityDelay(long timeout) {
        subscribeNetworkTask(getClass().getName().concat("jumpActivityDelay"),
                Observable.timer(timeout, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()),
                new MyObserver<Long>() {
                    @Override
                    public void onMyNext(Long aLong) {
                        if (mView != null) {
                            mView.delayToActivity();
                        }
                    }

                    @Override
                    public void onMyError(String errorMessage) {

                    }
                });

    }
}
