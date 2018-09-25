package com.wopin.qingpaopao.bean.response;

import com.wopin.qingpaopao.BuildConfig;

public class UploadImageRsp extends NormalRsp {


    /**
     * url : /images/153787260317621537872603331.jpg
     */

    private String url;

    public String getUrl() {
        return BuildConfig.basicUrlHttps.concat(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
