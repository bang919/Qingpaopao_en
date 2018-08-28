package com.wopin.qingpaopao.model;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.request.ThirdReq;
import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.bean.response.ThirdBindRsp;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.http.HttpClient;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ThirdLoginModel {

    public void loginByThird(final Platform platform, final ThirdLoginCallback thirdLoginCallback) {
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
                thirdLoginCallback.onThirdSuccess(platformName, userName, userId, userIcon);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                String errorMsg = throwable.toString();
                if (errorMsg.contains("WechatClientNotExistException")) {
                    errorMsg = MyApplication.getMyApplicationContext().getString(R.string.wechat_client_not_exist);
                }
                Disposable subscribe = Observable.just(errorMsg)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                thirdLoginCallback.onFailure(s);
                            }
                        });
            }

            @Override
            public void onCancel(Platform platform, int i) {
                thirdLoginCallback.onFailure(MyApplication.getMyApplicationContext().getString(R.string.cancel));
            }
        });
        platform.showUser(null);
    }

    public Observable<ThirdBindRsp> getThirdBind() {
        return HttpClient.getApiInterface().getThirdBinding()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NormalRsp> thirdBinding(String platformName, String userId) {
        ThirdReq thirdReq = new ThirdReq();
        thirdReq.setType(platformName);
        thirdReq.setKey(userId);
        return HttpClient.getApiInterface().thirdBinding(thirdReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface ThirdLoginCallback {
        void onThirdSuccess(String platformName, String userName, String userId, String userIcon);

        void onFailure(String errorMessage);
    }
}
