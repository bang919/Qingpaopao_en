package com.wopin.qingpaopao.bean.response;

import java.util.List;

public class ThirdBindRsp extends NormalRsp {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * _id : 5b825a9ebaa74c7d3f3e1304
         * uId : 5b825a9ebaa74c7d3f3e1303
         * thirdKey : 121DC8CAFBA7596AE05BA77A110CDF94
         * thirdType : QQ
         * __v : 0
         */

        private String _id;
        private String uId;
        private String thirdKey;
        private String thirdType;
        private int __v;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getThirdKey() {
            return thirdKey;
        }

        public void setThirdKey(String thirdKey) {
            this.thirdKey = thirdKey;
        }

        public String getThirdType() {
            return thirdType;
        }

        public void setThirdType(String thirdType) {
            this.thirdType = thirdType;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }
    }
}
