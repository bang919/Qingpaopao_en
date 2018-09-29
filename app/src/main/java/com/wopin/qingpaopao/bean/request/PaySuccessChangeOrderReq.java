package com.wopin.qingpaopao.bean.request;

public class PaySuccessChangeOrderReq {

    /**
     * orderId : 1214521
     * status : 1
     */

    private String orderId;
    private int status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
