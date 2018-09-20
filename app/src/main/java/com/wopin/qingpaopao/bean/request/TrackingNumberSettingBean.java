package com.wopin.qingpaopao.bean.request;

public class TrackingNumberSettingBean {

    /**
     * orderId : 1234
     * expressId : 12314142
     * expressName : bbbb
     */

    private String orderId;
    private String expressId;
    private String expressName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }
}
