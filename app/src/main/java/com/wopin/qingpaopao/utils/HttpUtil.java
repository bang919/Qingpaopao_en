package com.wopin.qingpaopao.utils;

import android.content.Context;
import android.text.TextUtils;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.common.MyApplication;
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
                if (myObserver != null)
                    myObserver.onMyNext(t);
            }

            @Override
            public void onError(Throwable e) {
                if (myObserver != null)
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
                        String errorMsg = normalRsp.getMsg();
                        Context context = MyApplication.getMyApplicationContext();
                        switch (status) {
                            case "414":
                                errorMsg = context.getString(R.string.error_414);
                                break;
                            case "511":
                                errorMsg = context.getString(R.string.error_511);
                                break;
                            case "512":
                                errorMsg = context.getString(R.string.error_512);
                                break;
                        }

                        onError(new Throwable(errorMsg));
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
