package com.wopin.qingpaopao.bean.response;

import java.util.List;

public class CrowdfundingOrderTotalRsp {


    /**
     * status : 0
     * msg :
     * result : [{"_id":null,"totalPrice":5995}]
     */

    private String status;
    private String msg;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * _id : null
         * totalPrice : 5995
         */

        private Object _id;
        private int totalPrice;

        public Object get_id() {
            return _id;
        }

        public void set_id(Object _id) {
            this._id = _id;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
        }
    }
}
