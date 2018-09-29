package com.mywopin.com.cup.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.common.Constants;

import java.util.ArrayList;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private static ArrayList<WXPayEntryActivityCallback> mCallbacks = new ArrayList<>();

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);

        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(final BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (WXPayEntryActivityCallback wxPayEntryActivityCallback : mCallbacks) {
                    wxPayEntryActivityCallback.onResp(resp);
                }
                finish();
            }
        });
    }

    public static void putCallback(WXPayEntryActivityCallback wxPayEntryActivityCallback) {
        mCallbacks.add(wxPayEntryActivityCallback);
    }

    public static void removeCallback(WXPayEntryActivityCallback wxPayEntryActivityCallback) {
        mCallbacks.remove(wxPayEntryActivityCallback);
    }

    public static void clearCallback() {
        mCallbacks.clear();
    }

    public interface WXPayEntryActivityCallback {
        void onResp(BaseResp resp);
    }
}
