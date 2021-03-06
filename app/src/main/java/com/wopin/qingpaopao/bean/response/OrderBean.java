package com.wopin.qingpaopao.bean.response;

import java.io.Serializable;

public class OrderBean implements Serializable {
    /**
     * _id : 5ba1162be76a104389daa0f0
     * orderId : 6220201809182313475
     * userId : 5b819f0591297b561a79eeaa
     * title : 氢popo 富氢水杯E款 真正的智能水杯
     * image : https://wifi.h2popo.com/wp-content/uploads/2018/07/富氢水杯详情页-01_01-253x300.jpg
     * goodsId : 127
     * address : {"_id":"5b9e314de659266b8d416f76","isDefault":true,"tel":15554,"address2":"聽起來","address1":"河北省邢台市临城县","userName":"經濟","addressId":"E20fNlm2OqwCv2lH"}
     * num : 1
     * offerPrice : 0
     * singlePrice : 999
     * orderStatus : 等待付款
     * createDate : 2018-09-18 23:13:47
     * createTime : 1537283627806
     * __v : 0
     */

    private String _id;
    private String orderId;
    private String userId;
    private String title;
    private String image;
    private int goodsId;
    private AddressBean address;
    private String num;
    private String offerPrice;
    private String singlePrice;
    private String orderStatus;
    private String createDate;
    private String expressReturnId;
    private String expressReturnName;
    private String infoBuyTime;
    private String infoCupColor;
    private String infoCupModel;
    private String infoPhone;
    private String infoSex;
    private String infoUsage;
    private String infoUserName;
    private long createTime;
    private int __v;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public int getFinalPrice() {
        return Integer.valueOf(singlePrice == null ? "0" : singlePrice) * Integer.valueOf(num == null ? "0" : num)
                - Integer.valueOf(offerPrice == null ? "0" : offerPrice);
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getExpressReturnId() {
        return expressReturnId;
    }

    public void setExpressReturnId(String expressReturnId) {
        this.expressReturnId = expressReturnId;
    }

    public String getExpressReturnName() {
        return expressReturnName;
    }

    public void setExpressReturnName(String expressReturnName) {
        this.expressReturnName = expressReturnName;
    }

    public String getInfoBuyTime() {
        return infoBuyTime;
    }

    public void setInfoBuyTime(String infoBuyTime) {
        this.infoBuyTime = infoBuyTime;
    }

    public String getInfoCupColor() {
        return infoCupColor;
    }

    public void setInfoCupColor(String infoCupColor) {
        this.infoCupColor = infoCupColor;
    }

    public String getInfoCupModel() {
        return infoCupModel;
    }

    public void setInfoCupModel(String infoCupModel) {
        this.infoCupModel = infoCupModel;
    }

    public String getInfoPhone() {
        return infoPhone;
    }

    public void setInfoPhone(String infoPhone) {
        this.infoPhone = infoPhone;
    }

    public String getInfoSex() {
        return infoSex;
    }

    public void setInfoSex(String infoSex) {
        this.infoSex = infoSex;
    }

    public String getInfoUsage() {
        return infoUsage;
    }

    public void setInfoUsage(String infoUsage) {
        this.infoUsage = infoUsage;
    }

    public String getInfoUserName() {
        return infoUserName;
    }

    public void setInfoUserName(String infoUserName) {
        this.infoUserName = infoUserName;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public static class AddressBean implements Serializable {
        /**
         * _id : 5b9e314de659266b8d416f76
         * isDefault : true
         * tel : 15554
         * address2 : 聽起來
         * address1 : 河北省邢台市临城县
         * userName : 經濟
         * addressId : E20fNlm2OqwCv2lH
         */

        private String _id;
        private boolean isDefault;
        private String tel;
        private String address2;
        private String address1;
        private String userName;
        private String addressId;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public boolean isIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }
    }


}
