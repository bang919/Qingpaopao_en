package com.wopin.qingpaopao.bean.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginRsp extends NormalRsp implements Serializable {
    private static final long serialVersionUID = 15943528961L;


    /**
     * result : {"_id":"5b819f0591297b561a79eeaa","phone":15989082970,"drinks":5,"scores":2004,"userName":"yuskkw","userPwd":"e10adc3949ba59abbe56e057f20f883e","__v":2,"collectBlogList":[],"likeCommentList":[],"lastAttendance":"20180911","addressList":[{"addressId":"KtfH9L1xSxbz4sq3","userName":"","address1":"选择地区","address2":"","tel":null,"isDefault":false,"_id":"5b9e26eb0510dc68797d3ea1"}],"cartList":[],"cupList":[],"fansList":[],"followList":[]}
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
         * drinks : 5
         * scores : 2004
         * userName : yuskkw
         * userPwd : e10adc3949ba59abbe56e057f20f883e
         * __v : 2
         * collectBlogList : []
         * likeCommentList : []
         * lastAttendance : 20180911
         * addressList : [{"addressId":"KtfH9L1xSxbz4sq3","userName":"","address1":"选择地区","address2":"","tel":null,"isDefault":false,"_id":"5b9e26eb0510dc68797d3ea1"}]
         * cartList : []
         * cupList : []
         * fansList : []
         * followList : []
         */

        private String _id;
        private String phone;
        private int drinks;
        private int scores;
        private String userName;
        private String userPwd;
        private String icon;
        private int __v;
        private String lastAttendance;
        private List<?> collectBlogList;
        private List<?> likeCommentList;
        private ArrayList<AddressListBean> addressList;
        private List<?> cartList;
        private List<?> cupList;
        private List<?> fansList;
        private List<?> followList;
        private ProfilesBean profiles;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
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

        public String getLastAttendance() {
            return lastAttendance;
        }

        public void setLastAttendance(String lastAttendance) {
            this.lastAttendance = lastAttendance;
        }

        public List<?> getCollectBlogList() {
            return collectBlogList;
        }

        public void setCollectBlogList(List<?> collectBlogList) {
            this.collectBlogList = collectBlogList;
        }

        public List<?> getLikeCommentList() {
            return likeCommentList;
        }

        public void setLikeCommentList(List<?> likeCommentList) {
            this.likeCommentList = likeCommentList;
        }

        public ArrayList<AddressListBean> getAddressList() {
            return addressList;
        }

        public void setAddressList(ArrayList<AddressListBean> addressList) {
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

        public ProfilesBean getProfiles() {
            return profiles;
        }

        public void setProfiles(ProfilesBean profiles) {
            this.profiles = profiles;
        }

        public static class AddressListBean implements Serializable {
            private static final long serialVersionUID = 15942628961L;
            /**
             * addressId : KtfH9L1xSxbz4sq3
             * userName :
             * address1 : 选择地区
             * address2 :
             * tel : null
             * isDefault : false
             * _id : 5b9e26eb0510dc68797d3ea1
             */

            private String addressId;
            private String userName;
            private String address1;
            private String address2;
            private String tel;
            private boolean isDefault;
            private String _id;

            public String getAddressId() {
                return addressId;
            }

            public void setAddressId(String addressId) {
                this.addressId = addressId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getAddress1() {
                return address1;
            }

            public void setAddress1(String address1) {
                this.address1 = address1;
            }

            public String getAddress2() {
                return address2;
            }

            public void setAddress2(String address2) {
                this.address2 = address2;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }

            public boolean isIsDefault() {
                return isDefault;
            }

            public void setIsDefault(boolean isDefault) {
                this.isDefault = isDefault;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }
        }

        public static class ProfilesBean implements Serializable {
            private static final long serialVersionUID = 159454618961L;

            private String height = "0";
            private String weight = "0";
            private String age = "0";
            private String blood_sugar_full = "0";
            private String blood_sugar_hugry = "0";
            private String blood_lipid_all = "0";//总胆固醇
            private String blood_lipid = "0";//胆固醇脂
            private String blood_lipid_TG = "0";//甘油三酯
            private String blood_pressure = "0";
            private String blood_pressure_press = "0";

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public String getBlood_sugar_full() {
                return blood_sugar_full;
            }

            public void setBlood_sugar_full(String blood_sugar_full) {
                this.blood_sugar_full = blood_sugar_full;
            }

            public String getBlood_sugar_hugry() {
                return blood_sugar_hugry;
            }

            public void setBlood_sugar_hugry(String blood_sugar_hugry) {
                this.blood_sugar_hugry = blood_sugar_hugry;
            }

            public String getBlood_lipid_all() {
                return blood_lipid_all;
            }

            public void setBlood_lipid_all(String blood_lipid_all) {
                this.blood_lipid_all = blood_lipid_all;
            }

            public String getBlood_lipid() {
                return blood_lipid;
            }

            public void setBlood_lipid(String blood_lipid) {
                this.blood_lipid = blood_lipid;
            }

            public String getBlood_lipid_TG() {
                return blood_lipid_TG;
            }

            public void setBlood_lipid_TG(String blood_lipid_TG) {
                this.blood_lipid_TG = blood_lipid_TG;
            }

            public String getBlood_pressure() {
                return blood_pressure;
            }

            public void setBlood_pressure(String blood_pressure) {
                this.blood_pressure = blood_pressure;
            }

            public String getBlood_pressure_press() {
                return blood_pressure_press;
            }

            public void setBlood_pressure_press(String blood_pressure_press) {
                this.blood_pressure_press = blood_pressure_press;
            }
        }
    }
}
