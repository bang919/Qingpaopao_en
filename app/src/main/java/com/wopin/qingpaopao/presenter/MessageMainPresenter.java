package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.common.Constants;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.model.MessageMainModel;
import com.wopin.qingpaopao.utils.SPUtils;
import com.wopin.qingpaopao.view.MessageMainView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class MessageMainPresenter extends BasePresenter<MessageMainView> {

    private MessageMainModel mMessageMainModel;

    public MessageMainPresenter(Context context, MessageMainView view) {
        super(context, view);
        mMessageMainModel = new MessageMainModel();
    }

    public void getMessages() {

        Observable<String> zip = Observable.zip(
                mMessageMainModel.getSystemMessage().doOnNext(new Consumer<SystemMessageRsp>() {
                    @Override
                    public void accept(SystemMessageRsp systemMessageRsp) throws Exception {
                        mView.onSystemMessage(systemMessageRsp);
                    }
                }),
                mMessageMainModel.getNewCommentNotify()
                        .doOnNext(new Consumer<NewCommentRsp>() {
                            @Override
                            public void accept(NewCommentRsp newCommentRsp) throws Exception {
                                if (newCommentRsp.getResult() != null) {
                                    ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newComments = newCommentRsp.getResult().getNewComment();
                                    ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newCommentStores = getNewCommentRsps();
                                    if (newCommentStores != null) {
                                        newComments.addAll(newCommentStores);
                                    }
                                    storeNewCommentRsps(newComments);
                                    mView.onNewCommentCount(newComments);
                                }
                            }
                        }),
                new BiFunction<SystemMessageRsp, NewCommentRsp, String>() {
                    @Override
                    public String apply(SystemMessageRsp systemMessageRsp, NewCommentRsp newCommentRsp) throws Exception {
                        return "Success";
                    }
                });
        subscribeNetworkTask(getClass().getSimpleName().concat("getMessages"), zip, new MyObserver<String>() {
            @Override
            public void onMyNext(String s) {
                mView.onFinishRequest();
            }

            @Override
            public void onMyError(String errorMessage) {

                mView.onError(errorMessage);
            }
        });
    }

    public static ArrayList<NewCommentRsp.ResultBean.NewCommentBean> getNewCommentRsps() {
        try {
            return SPUtils.getObject(MyApplication.getMyApplicationContext(), Constants.NEW_COMMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void storeNewCommentRsps(ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newCommentBeans) {
        SPUtils.putObject(MyApplication.getMyApplicationContext(), Constants.NEW_COMMENT, newCommentBeans);
    }

    public static void clearNewCommentRsps() {
        SPUtils.remove(MyApplication.getMyApplicationContext(), Constants.NEW_COMMENT);
    }
}
