package com.wopin.qingpaopao.bean.request;

public class CupUpdateReq {

    /**
     * type : bluetooth
     * uuid : 123
     * name : hello
     * add : true
     */
    public static final String WIFI = "WIFI";
    public static final String BLE = "BLE";
    private String type;
    private String uuid;
    private String name;
    private String address;
    private boolean add;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}
