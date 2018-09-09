package com.wopin.qingpaopao.fragment.drinking;

import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.MyFragmentPageAdapter;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.manager.WifiConnectManager;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.WifiSettingSuccessListener;

public class WifiFragmentPageRoot extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "WifiFragmentPageRoot";
    private MyFragmentPageAdapter mMyFragmentPageAdapter;
    private WifiSettingSuccessListener mWifiSettingSuccessListener;

    public void setWifiSettingSuccessListener(WifiSettingSuccessListener wifiSettingSuccessListener) {
        mWifiSettingSuccessListener = wifiSettingSuccessListener;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.link_wifi);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_wifi_rootpage;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {
        mMyFragmentPageAdapter = new MyFragmentPageAdapter(this, R.id.content_layout);
        WifiFragmentPage1 wifiFragmentPage1 = new WifiFragmentPage1();
        wifiFragmentPage1.setOnClickListener(this);
        mMyFragmentPageAdapter.switchToFragment(wifiFragmentPage1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step_btn://第一页按了next
                WifiFragmentPage2 wifiFragmentPage2 = new WifiFragmentPage2();
                wifiFragmentPage2.setOnClickListener(this);
                wifiFragmentPage2.setWifiSettingSuccessListener(new WifiSettingSuccessListener() {
                    @Override
                    public void onWifiSettingSuccess(WifiRsp wifiRsp) {
                        mWifiSettingSuccessListener.onWifiSettingSuccess(wifiRsp);
                        dismiss();
                    }
                });
                mMyFragmentPageAdapter.switchToFragment(wifiFragmentPage2, true);
                break;
            case R.id.btn_scan://第二页扫描按钮
                WifiFragmentPageScan wifiFragmentPageScan = new WifiFragmentPageScan();
                wifiFragmentPageScan.setWifiScanCallback(new WifiFragmentPageScan.WifiScanCallback() {
                    @Override
                    public void onWifiScanCallback(String ssid, String password, String encryption) {
                        ToastUtils.showLong(String.format(getString(R.string.trying_connect), ssid));
                        WifiConnectManager.getInstance().connectDevice(ssid, password);
                        getChildFragmentManager().popBackStackImmediate();//退出WifiFragmentPageScan
                    }
                });
                mMyFragmentPageAdapter.switchToFragment(wifiFragmentPageScan, true);
                break;
            case R.id.btn_link_by_hand://第二页手动连接按钮
                new LinkWifiByhandFragment().show(getChildFragmentManager(), LinkWifiByhandFragment.TAG);
                break;
        }
    }
}
