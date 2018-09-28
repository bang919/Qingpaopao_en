package com.wopin.qingpaopao.wxapi;

import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

//微信支付返回
public class WXPayEntryActivity {
    private static final String TAG = "WXPayEntryActivity";

    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.d(TAG, "onPayFinish,errCode=" + resp.errCode);
        }
    }
}
