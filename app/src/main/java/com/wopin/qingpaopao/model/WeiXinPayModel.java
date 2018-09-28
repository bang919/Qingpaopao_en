package com.wopin.qingpaopao.model;

import android.content.Context;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wopin.qingpaopao.bean.response.OrderResponse;

public class WeiXinPayModel {

    // http://wifi.h2popo.com:8081/getWeChatPaySign
    public void pay(Context context, OrderResponse.OrderBean orderBean) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
// 将该app注册到微信
        msgApi.registerApp("wxd930ea5d5a258f4f");


//        PayReq request = new PayReq();
//        request.appId = "wxd930ea5d5a258f4f";
//        request.partnerId = "1900000109";
//        request.prepayId = "1101000000140415649af9fc314aa427";
//        request.packageValue = "Sign=WXPay";
//        request.nonceStr = "1101000000140429eb40476f8896f4c9";
//        request.timeStamp = "1398746574";
//        request.sign = "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
//        msgApi.sendReq(request);
    }
}
