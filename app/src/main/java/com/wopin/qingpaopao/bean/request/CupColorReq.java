package com.wopin.qingpaopao.bean.request;

public class CupColorReq {

    /**
     * uuid : abcd
     * color : 1
     */

    private String uuid;
    private int color;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCupColor() {
        return color;
    }

    public void setCupColor(int cupColor) {
        this.color = cupColor;
    }
}
