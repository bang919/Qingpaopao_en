package com.wopin.qingpaopao.bean.response;

import java.io.Serializable;

public class NormalRsp implements Serializable {
    private static final long serialVersionUID = 15473148961L;

    /**
     * status : 0
     * msg :
     */

    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
