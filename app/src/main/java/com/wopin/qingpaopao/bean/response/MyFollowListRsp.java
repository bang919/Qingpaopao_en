package com.wopin.qingpaopao.bean.response;

import java.util.List;

public class MyFollowListRsp extends NormalRsp{

    private List<MyFollowBean> result;

    public List<MyFollowBean> getMyFollowBeans() {
        return result;
    }

    public void setMyFollowBeans(List<MyFollowBean> result) {
        this.result = result;
    }

    public static class MyFollowBean {
        /**
         * _id : 5b66bbf64238af6a804aacaf
         * userName : admin
         */

        private String _id;
        private String userName;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
