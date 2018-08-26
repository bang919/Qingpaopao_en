package com.wopin.qingpaopao.adapter;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * 不要删！！消除"power by sharesdk"字样
 */
public class MyAuthorizeAdapter extends AuthorizeAdapter {

    @Override
    public void onCreate() {
        hideShareSDKLogo();
    }
}
