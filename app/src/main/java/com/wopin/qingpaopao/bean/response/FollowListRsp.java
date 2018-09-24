package com.wopin.qingpaopao.bean.response;

import java.util.ArrayList;

public class FollowListRsp extends NormalRsp {

    private ArrayList<MyFollowBean> result;

    public ArrayList<MyFollowBean> getFollowBeans() {
        return result;
    }

    public void setFollowBeans(ArrayList<MyFollowBean> result) {
        this.result = result;
    }

    public static class MyFollowBean {
        /**
         * _id : 5b66bbf64238af6a804aacaf
         * userName : admin
         */

        private String _id;
        private String userName;
        private String icon;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
