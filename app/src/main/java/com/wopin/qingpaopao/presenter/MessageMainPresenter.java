package com.wopin.qingpaopao.presenter;

import android.content.Context;

import com.wopin.qingpaopao.bean.response.SystemMessageRsp;
import com.wopin.qingpaopao.model.MessageMainModel;
import com.wopin.qingpaopao.view.MessageMainView;

public class MessageMainPresenter extends BasePresenter<MessageMainView> {

    private MessageMainModel mMessageMainModel;

    public MessageMainPresenter(Context context, MessageMainView view) {
        super(context, view);
        mMessageMainModel = new MessageMainModel();
    }

    public void getSystemMessage() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getSystemMessage"), mMessageMainModel.getSystemMessage(), new MyObserver<SystemMessageRsp>() {
            @Override
            public void onMyNext(SystemMessageRsp systemMessageRsp) {
                mView.onSystemMessage(systemMessageRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
