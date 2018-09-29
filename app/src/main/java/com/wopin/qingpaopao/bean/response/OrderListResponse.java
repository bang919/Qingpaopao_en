package com.wopin.qingpaopao.bean.response;

import java.util.ArrayList;

public class OrderListResponse extends NormalRsp{

    /**
     * status : 0
     * msg :
     * result : [{"_id":"5ba1162be76a104389daa0f0","orderId":"6220201809182313475","userId":"5b819f0591297b561a79eeaa","title":"氢popo 富氢水杯E款 真正的智能水杯","image":"https://wifi.h2popo.com/wp-content/uploads/2018/07/富氢水杯详情页-01_01-253x300.jpg","goodsId":127,"address":{"_id":"5b9e314de659266b8d416f76","isDefault":true,"tel":15554,"address2":"聽起來","address1":"河北省邢台市临城县","userName":"經濟","addressId":"E20fNlm2OqwCv2lH"},"num":1,"offerPrice":0,"singlePrice":999,"orderStatus":"等待付款","createDate":"2018-09-18 23:13:47","createTime":1537283627806,"__v":0}]
     */

    private ArrayList<OrderBean> result;

    public ArrayList<OrderBean> getOrderBeans() {
        return result;
    }

    public void setOrderBeans(ArrayList<OrderBean> result) {
        this.result = result;
    }
}
