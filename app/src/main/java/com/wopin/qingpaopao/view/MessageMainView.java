package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.NewCommentRsp;
import com.wopin.qingpaopao.bean.response.SystemMessageRsp;

import java.util.ArrayList;

public interface MessageMainView {

    void onNewCommentCount(ArrayList<NewCommentRsp.ResultBean.NewCommentBean> newCommentBeans);

    void onSystemMessage(SystemMessageRsp systemMessageRsp);

    void onFinishRequest();

    void onError(String errorMessage);
}
