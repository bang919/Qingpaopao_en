package com.wopin.qingpaopao.bean.request;

public class AddressBean {

    /**
     * addressId : dfjasiofjadso
     * userName : bigbang
     * address1 : 广东省佛山市南海区
     * address2 : 我家我家我家
     * tel : 15888888888
     * isDefault : true
     */

    private String addressId;
    private String userName;
    private String address1;
    private String address2;
    private String tel;
    private boolean isDefault;

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
}
