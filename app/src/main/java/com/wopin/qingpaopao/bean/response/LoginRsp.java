package com.wopin.qingpaopao.bean.response;

import java.io.Serializable;
import java.util.List;

public class LoginRsp extends NormalRsp implements Serializable {
    private static final long serialVersionUID = 15943528961L;

    /**
     * result : {"_id":"5b819f0591297b561a79eeaa","phone":15989082970,"drinks":0,"scores":0,"userName":"15989082970","userPwd":"e10adc3949ba59abbe56e057f20f883e","__v":0,"addressList":[],"cartList":[],"cupList":[],"fansList":[],"followList":[]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        private static final long serialVersionUID = 1523148961L;
        /**
         * _id : 5b819f0591297b561a79eeaa
         * phone : 15989082970
         * drinks : 0
         * scores : 0
         * userName : 15989082970
         * userPwd : e10adc3949ba59abbe56e057f20f883e
         * __v : 0
         * addressList : []
         * cartList : []
         * cupList : []
         * fansList : []
         * followList : []
         */

        private String _id;
        private long phone;
        private int drinks;
        private int scores;
        private String userName;
        private String userPwd;
        private String icon;
        private int __v;
        private List<?> addressList;
        private List<?> cartList;
        private List<?> cupList;
        private List<?> fansList;
        private List<?> followList;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public int getDrinks() {
            return drinks;
        }

        public void setDrinks(int drinks) {
            this.drinks = drinks;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPwd() {
            return userPwd;
        }

        public void setUserPwd(String userPwd) {
            this.userPwd = userPwd;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public List<?> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<?> addressList) {
            this.addressList = addressList;
        }

        public List<?> getCartList() {
            return cartList;
        }

        public void setCartList(List<?> cartList) {
            this.cartList = cartList;
        }

        public List<?> getCupList() {
            return cupList;
        }

        public void setCupList(List<?> cupList) {
            this.cupList = cupList;
        }

        public List<?> getFansList() {
            return fansList;
        }

        public void setFansList(List<?> fansList) {
            this.fansList = fansList;
        }

        public List<?> getFollowList() {
            return followList;
        }

        public void setFollowList(List<?> followList) {
            this.followList = followList;
        }
    }
}
