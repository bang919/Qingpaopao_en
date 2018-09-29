package com.wopin.qingpaopao.bean.response;

public class OrderOneResponse extends NormalRsp {

    /**
     * result : {"__v":0,"orderId":"8222201809291518597","userId":"5bacb57f38ec47333104e4e7","title":"智能骑行旅行箱一个","image":"http://img10.360buyimg.com/popWaterMark/jfs/t21511/30/1763708646/394955/bd472da3/5b348a7fNcb7b6145.jpg","goodsId":196,"address":{"addressId":"3j7uR4I3NbH5a6mg","userName":"mmnn ","address1":"河北省邢台市临城县","address2":"hsjsnmsmd","tel":1596634557,"isDefault":true,"_id":"5bacf7b7750bd201cda77f09"},"num":1,"singlePrice":2350,"orderStatus":"等待付款","createDate":"2018-09-29 15:18:59","createTime":1538205539456,"_id":"5baf2763866b033d23cc2f0a"}
     */

    private OrderBean result;

    public OrderBean getResult() {
        return result;
    }

    public void setResult(OrderBean result) {
        this.result = result;
    }
}
