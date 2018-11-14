package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.CheckNewMessageRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTodayRsp;
import com.wopin.qingpaopao.bean.response.DrinkListTotalRsp;
import com.wopin.qingpaopao.bean.response.LoginRsp;
import com.wopin.qingpaopao.model.DrinkingModel;
import com.wopin.qingpaopao.model.MessageModel;
import com.wopin.qingpaopao.model.MineModel;
import com.wopin.qingpaopao.view.MineView;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public class MinePresenter extends BasePresenter<MineView> {

    private MessageModel mMessageModel;

    public MinePresenter(Context context, MineView view) {
        super(context, view);
        mMessageModel = new MessageModel();
    }

    public void requestDrinkData() {
        DrinkingModel drinkingModel = new DrinkingModel();
        subscribeNetworkTask(getClass().getSimpleName().concat("requestDrinkData"),
                Observable.zip(drinkingModel.getDrinkList(), drinkingModel.getTodayDrinkList(),
                        new BiFunction<DrinkListTotalRsp, DrinkListTodayRsp, String>() {
                            @Override
                            public String apply(DrinkListTotalRsp drinkListTotalRsp, DrinkListTodayRsp drinkListTodayRsp) throws Exception {
                                mView.onTotalDrink(drinkListTotalRsp);
                                mView.onTodayDrink(drinkListTodayRsp);
                                return "Success";
                            }
                        }), new MyObserver<String>() {
                    @Override
                    public void onMyNext(String s) {

                    }

                    @Override
                    public void onMyError(String errorMessage) {
                        mView.onError(errorMessage);
                    }
                });

        subscribeNetworkTask(getClass().getSimpleName().concat("checkNewMessage"), mMessageModel.checkNewMessage(), new MyObserver<CheckNewMessageRsp>() {
            @Override
            public void onMyNext(CheckNewMessageRsp checkNewMessageRsp) {
                mView.onNewMessage(checkNewMessageRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void refreshUserData() {
        Observable<LoginRsp> userDataOb = new MineModel().getUserData();
        subscribeNetworkTask(getClass().getSimpleName().concat("refreshUserData"), userDataOb, new MyObserver<LoginRsp>() {
            @Override
            public void onMyNext(LoginRsp loginRsp) {
                mView.onRefreshUserData();
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
