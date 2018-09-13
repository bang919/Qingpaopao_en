package com.wopin.qingpaopao.fragment.drinking;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.WifiConfigToCupRsp;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.WifiPageChooseListPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.view.WifiPageChooseListView;
import com.wopin.qingpaopao.view.WifiSettingSuccessListener;

import java.util.ArrayList;

public class WifiFragmentPageChooseList extends BaseBarDialogFragment<WifiPageChooseListPresenter> implements WifiPageChooseListView, View.OnClickListener, WifiFragmentPageChooseListDialog.WifiChooseCallback {

    public static final String TAG = "WifiFragmentPageChooseList";
    private ArrayList<WifiRsp> wifiRsps;
    private TextView mChooseWifiTv;
    private EditText mPasswordEt;
    private View mProgressBar;
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
        return R.layout.fragment_wifi_page_choose_list;
    }

    @Override
    protected WifiPageChooseListPresenter initPresenter() {
        return new WifiPageChooseListPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mChooseWifiTv = rootView.findViewById(R.id.tv_click_to_choose_wifi);
        mPasswordEt = rootView.findViewById(R.id.et_wifi_password);
        mProgressBar = rootView.findViewById(R.id.progress_bar_layout);
        rootView.findViewById(R.id.tv_click_to_choose_wifi).setOnClickListener(this);
        rootView.findViewById(R.id.bt_link).setOnClickListener(this);
    }

    public void setProgressbarVis(boolean isVisibility) {
        mProgressBar.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        ToastUtils.showShort(getString(R.string.success_connect_wifi));
        setProgressbarVis(true);
        mPresenter.getWifiList();
        mProgressBar.requestFocus();
    }

    @Override
    public void onWifiListResponse(ArrayList<WifiRsp> wifiRsps) {
        this.wifiRsps = wifiRsps;
        setProgressbarVis(false);
    }

    @Override
    public void onWifiConfigToCupRsp(WifiConfigToCupRsp wifiConfigToCupRsp) {
        ToastUtils.showShort(R.string.success_setting_wifi);
        setProgressbarVis(false);
        mWifiSettingSuccessListener.onWifiSettingSuccess(wifiConfigToCupRsp);
        dismiss();
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
        setProgressbarVis(false);
    }

    @Override
    public void onClick(View v) {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_click_to_choose_wifi://选择wifi按钮
                WifiFragmentPageChooseListDialog wifiFragmentPageChooseListDialog = WifiFragmentPageChooseListDialog.getInstance(wifiRsps);
                wifiFragmentPageChooseListDialog.setWifiChooseCallback(this);
                wifiFragmentPageChooseListDialog.show(getChildFragmentManager(), WifiFragmentPageChooseListDialog.TAG);
                break;
            case R.id.bt_link://点击连接按钮
                mPresenter.sendWifiConfigToCup(mChooseWifiTv.getText().toString(), mPasswordEt.getText().toString());
                break;
        }
    }

    @Override
    public void onWifiChoose(WifiRsp wifiRsp) {
        //点击选择了一个wifi
        mChooseWifiTv.setTextColor(Color.BLACK);
        mChooseWifiTv.setText(wifiRsp.getEssid());
    }
}
