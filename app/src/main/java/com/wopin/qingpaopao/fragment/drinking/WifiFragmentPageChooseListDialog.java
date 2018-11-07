package com.wopin.qingpaopao.fragment.drinking;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.WifiChooseListRvAdapter;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.dialog.NormalEdittextDialog;
import com.wopin.qingpaopao.fragment.BaseDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;

import java.util.ArrayList;

public class WifiFragmentPageChooseListDialog extends BaseDialogFragment implements View.OnClickListener {

    public static final String TAG = "WifiFragmentPageChooseListDialog";
    private RecyclerView mChooseListRv;
    private WifiChooseCallback mWifiChooseCallback;

    public static WifiFragmentPageChooseListDialog getInstance(ArrayList<WifiRsp> wifiRsps) {
        WifiFragmentPageChooseListDialog wifiFragmentPageChooseListDialog = new WifiFragmentPageChooseListDialog();
        Bundle args = new Bundle();
        args.putSerializable(TAG, wifiRsps);
        wifiFragmentPageChooseListDialog.setArguments(args);
        return wifiFragmentPageChooseListDialog;
    }

    public void setWifiChooseCallback(WifiChooseCallback wifiChooseCallback) {
        mWifiChooseCallback = wifiChooseCallback;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_wifi_page_choose_list_dialog;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mChooseListRv = rootView.findViewById(R.id.rv_wifi_choose_list);
        rootView.findViewById(R.id.bt_cancel).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        mChooseListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<WifiRsp> wifiRsps = (ArrayList<WifiRsp>) getArguments().getSerializable(TAG);
        mChooseListRv.setAdapter(new WifiChooseListRvAdapter(wifiRsps, new WifiChooseCallback() {
            @Override
            public void onWifiChoose(String ssid) {
                if (ssid == null && mWifiChooseCallback != null) {
                    new NormalEdittextDialog(getContext(), getString(R.string.confirm), getString(R.string.cancel), getString(R.string.input_ssid),
                            new NormalEdittextDialog.NormalEdittextDialogETextCallback() {
                                @Override
                                public void onEdittextInput(String etString) {
                                    mWifiChooseCallback.onWifiChoose(etString);
                                    dismiss();
                                }
                            }, null).show();
                } else if (mWifiChooseCallback != null) {
                    mWifiChooseCallback.onWifiChoose(ssid);
                    dismiss();
                }
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                dismiss();
                break;
        }
    }

    public interface WifiChooseCallback {
        void onWifiChoose(String ssid);
    }
}
