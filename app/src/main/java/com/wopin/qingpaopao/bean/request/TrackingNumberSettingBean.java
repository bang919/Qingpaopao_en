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
    private String infoUserName;
    private String infoPhone;

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

    public String getInfoUserName() {
        return infoUserName;
    }

    public void setInfoUserName(String infoUserName) {
        this.infoUserName = infoUserName;
    }

    public String getInfoPhone() {
        return infoPhone;
    }

    public void setInfoPhone(String infoPhone) {
        this.infoPhone = infoPhone;
    }
}
