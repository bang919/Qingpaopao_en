package com.wopin.qingpaopao.bean.response;

import java.util.List;

public class CrowdfundingOrderTotalPeopleRsp extends NormalRsp{


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
        private int totalPeople;

        public Object get_id() {
            return _id;
        }

        public void set_id(Object _id) {
            this._id = _id;
        }

        public int getTotalPeople() {
            return totalPeople;
        }

        public void setTotalPeople(int totalPrice) {
            this.totalPeople = totalPrice;
        }
    }
}
