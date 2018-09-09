package com.wopin.qingpaopao.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.WifiConfigToCupRsp;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.model.WifiCupPostModel;
import com.wopin.qingpaopao.view.WifiPageChooseListView;

import java.util.ArrayList;

public class WifiPageChooseListPresenter extends BasePresenter<WifiPageChooseListView> {

    private WifiCupPostModel mWifiCupPostModel;

    public WifiPageChooseListPresenter(Context context, WifiPageChooseListView view) {
        super(context, view);
        mWifiCupPostModel = new WifiCupPostModel();
    }

    public void getWifiList() {
        subscribeNetworkTask(getClass().getSimpleName().concat("getWifiList"), mWifiCupPostModel.getWifiList(), new MyObserver<ArrayList<WifiRsp>>() {
            @Override
            public void onMyNext(ArrayList<WifiRsp> wifiRsps) {
                mView.onWifiListResponse(wifiRsps);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }

    public void sendWifiConfigToCup(String ssid, String password) {
        if (TextUtils.isEmpty(ssid)) {
            mView.onError(mContext.getString(R.string.click_to_choose_wifi));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mView.onError(mContext.getString(R.string.please_input_password));
            return;
        }
        subscribeNetworkTask(getClass().getSimpleName().concat("sendWifiConfigToCup"), mWifiCupPostModel.sendWifiConfigToCup(ssid, password), new MyObserver<WifiConfigToCupRsp>() {
            @Override
            public void onMyNext(WifiConfigToCupRsp wifiConfigToCupRsp) {
                mView.onWifiConfigToCupRsp(wifiConfigToCupRsp);
            }

            @Override
            public void onMyError(String errorMessage) {
                mView.onError(errorMessage);
            }
        });
    }
}
