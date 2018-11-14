package com.wopin.qingpaopao.view;

import com.wopin.qingpaopao.bean.response.SystemMessageRsp;

public interface MessageMainView {

    void onSystemMessage(SystemMessageRsp systemMessageRsp);

    void onError(String errorMessage);
}
