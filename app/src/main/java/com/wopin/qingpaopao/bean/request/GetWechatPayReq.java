package com.wopin.qingpaopao.bean.request;

public class GetWechatPayReq {

    /**
     * amount : 123.4
     * subject : abc
     * body : hahahah
     * orderId : sjfiosjfiodsaj
     */

    private int amount;
    private String subject;
    private String body;
    private String orderId;

    public int getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = (int) amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
