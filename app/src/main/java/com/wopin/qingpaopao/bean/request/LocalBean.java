package com.wopin.qingpaopao.bean.request;

import com.google.gson.annotations.SerializedName;

public class LocalBean {

    /**
     * device_id : abc
     * time : 2018-10-09 00:18:54
     * lat : 46456.16
     * long : 12313.13
     * link : http://www.baidu.com
     */

    private String device_id;
    private String time;
    private String lat;
    @SerializedName("long")
    private String longX;
    private String link;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongX() {
        return longX;
    }

    public void setLongX(String longX) {
        this.longX = longX;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
