package com.wopin.qingpaopao.bean.response;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ErrorResponseBean {


    /**
     * status : 1001
     * msg : 当前未登录
     * result :
     */

    private String status;
    private String msg;
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
