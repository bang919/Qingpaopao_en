package com.wopin.qingpaopao.utils;

import android.text.TextUtils;

import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.presenter.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HttpUtil {

    /**
     * Request Http ， 不用MVP的时候可以用这个请求网络
     */
    public static <T> void subscribeNetworkTask(Observable<T> observable, final BasePresenter.MyObserver<T> myObserver) {
        observable.subscribe(handlerObserver(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(T t) {
                myObserver.onMyNext(t);
            }

            @Override
            public void onError(Throwable e) {
                myObserver.onMyError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public static <T> Observer<T> handlerObserver(final Observer<T> observer) {
        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                observer.onSubscribe(d);
            }

            @Override
            public void onNext(T t) {
                if (t instanceof NormalRsp) {
                    NormalRsp normalRsp = (NormalRsp) t;
                    String status = normalRsp.getStatus();
                    if (TextUtils.isEmpty(status) || Integer.valueOf(status) == 0) {
                        observer.onNext(t);
                    } else {
                        onError(new Throwable(normalRsp.getMsg()));
                    }
                } else {
                    observer.onNext(t);
                }
            }

            @Override
            public void onError(Throwable e) {
                String errorMessage = ExceptionUtil.getHttpExceptionMessage(e);
                observer.onError(new Throwable(errorMessage));
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        };
    }
}
