package com.wopin.qingpaopao.bean.response;

import java.io.Serializable;

public class WifiRsp implements Serializable {

    /**
     * essid :
     * bssid : 90:1d:ff:3f:00:00
     * rssi : 0
     * enc : 0
     * channel : 0
     */

    private String essid;
    private String bssid;
    private String rssi;
    private String enc;
    private String channel;

    public String getEssid() {
        return essid;
    }

    public void setEssid(String essid) {
        this.essid = essid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
