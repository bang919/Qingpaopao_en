package com.wopin.qingpaopao.bean.request;

public class ThirdReq {

    /**
     * userName : bigbang
     * key : 1234567890
     * type : QQ
     */

    private String userName;
    private String key;
    private String type;
    private String icon;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
