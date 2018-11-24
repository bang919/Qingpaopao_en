package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.NormalRsp;
import com.wopin.qingpaopao.model.SigninModel;
import com.wopin.qingpaopao.view.SigninView;

public class SigninPresenter extends BasePresenter<SigninView> {

    public SigninPresenter(Context context, SigninView view) {
        super(context, view);
    }

    public void signinCup(String uuid, String cupId) {
        subscribeNetworkTask(getClass().getSimpleName().concat("signinCup"), new SigninModel().signinCup(uuid, cupId), new MyObserver<NormalRsp>() {
            @Override
            public void onMyNext(NormalRsp normalRsp) {
                mView.onSigninSuccess();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
