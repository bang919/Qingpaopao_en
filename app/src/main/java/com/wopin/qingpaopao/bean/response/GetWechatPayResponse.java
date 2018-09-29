package com.wopin.qingpaopao.bean.response;

import com.google.gson.annotations.SerializedName;

public class GetWechatPayResponse extends NormalRsp {

    /**
     * result : {"partnerid":"1513314731","package":"Sign=WXPay","timestamp":1538149773,"prepayid":"wx28234933436441951b290e423414519424","noncestr":"dsiynkzFiy3UUoCb","sign":"EA886F30DF6A5ABF01D014DC1C8843CB"}
     */

    private WechatPayBean result;

    public WechatPayBean getWechatPayBean() {
        return result;
    }

    public void setWechatPayBean(WechatPayBean result) {
        this.result = result;
    }

    public static class WechatPayBean {
        /**
         * partnerid : 1513314731
         * package : Sign=WXPay
         * timestamp : 1538149773
         * prepayid : wx28234933436441951b290e423414519424
         * noncestr : dsiynkzFiy3UUoCb
         * sign : EA886F30DF6A5ABF01D014DC1C8843CB
         */

        private String partnerid;
        @SerializedName("package")
        private String packageX;
        private String timestamp;
        private String prepayid;
        private String noncestr;
        private String sign;

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
