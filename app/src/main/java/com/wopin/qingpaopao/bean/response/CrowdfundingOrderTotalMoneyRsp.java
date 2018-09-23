package com.wopin.qingpaopao.bean.response;

import java.util.List;

public class CrowdfundingOrderTotalMoneyRsp extends NormalRsp{


    /**
     * status : 0
     * msg :
     * result : [{"_id":null,"totalPrice":5995}]
     */

    private List<ResultBean> result;

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
