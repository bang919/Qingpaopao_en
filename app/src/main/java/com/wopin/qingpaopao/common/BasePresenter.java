package com.wopin.qingpaopao.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.utils.ExceptionUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by bigbang on 2017/5/18.
 * presenter 基类
 */

public class BasePresenter<V> {

    private final String TAG = "BasePresenter";

    protected V mView;
    protected Context mContext;

    public BasePresenter(Context context, V view) {
        mView = view;
        mContext = context;
    }

    protected <T extends NormalRsp> void subscribeNetworkTask(Observable<T> observable) {
        observable.retry(2).subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(T t) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public <T> void subscribeNetworkTask(String observerTag, Observable<T> observable, MyObserver<T> myObserver) {
        Observer<T> observer = createObserver(observerTag, myObserver);
        observable.subscribe(observer);
    }


    public void destroy() {
        cancelAllRequest();
        if (mView != null) {
            mView = null;
        }
    }


    /**
     * --------------------------------- DisposableMap ---------------------------------
     */

    private HashMap<String, Disposable> mDisposableMap = new HashMap<>();

    private void putObserver(String observerTag, Disposable disposable) {
        mDisposableMap.put(observerTag, disposable);
    }

    public void removeObserver(String observerTag) {
        Disposable d = mDisposableMap.get(observerTag);
        if (d != null && !d.isDisposed()) {
            d.dispose();
            mDisposableMap.remove(observerTag);
        }
        mDisposableMap.remove(observerTag);
    }

    public void cancelAllRequest() {
        Iterator<Map.Entry<String, Disposable>> iterator = mDisposableMap.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().dispose();
        }
        mDisposableMap.clear();
    }

    public <T> Observer<T> createObserver(final String observerTag, final MyObserver<T> observer) {

        removeObserver(observerTag);

        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                putObserver(observerTag, d);
            }

            @Override
            public void onNext(T t) {
                if (t instanceof NormalRsp) {
                    NormalRsp normalRsp = (NormalRsp) t;
                    String status = normalRsp.getStatus();
                    if (TextUtils.isEmpty(status) || Integer.valueOf(status) == 0) {
                        observer.onMyNext(t);
                    } else {
                        onError(new Throwable(normalRsp.getMsg()));
                    }
                } else {
                    observer.onMyNext(t);
                }
            }

            @Override
            public void onError(Throwable e) {
                String errorMessage = ExceptionUtil.getHttpExceptionMessage(e);
                observer.onMyError(errorMessage);
                removeObserver(observerTag);
            }

            @Override
            public void onComplete() {
                removeObserver(observerTag);
            }
        };
    }

    public interface MyObserver<T> {
        void onMyNext(T t);

        void onMyError(String errorMessage);
    }
}
