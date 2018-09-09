package com.wopin.qingpaopao.fragment.drinking;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.CupListAdapter;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.bean.response.WifiRsp;
import com.wopin.qingpaopao.widget.PlusItemSlideCallback;
import com.wopin.qingpaopao.widget.WItemTouchHelperPlus;

import java.util.ArrayList;

public class DrinkingListView extends Fragment implements View.OnClickListener {

    private View mRootView;
    private View mBackBtn;
    private RecyclerView mCupListRv;
    private Button mAddDeviceBt;
    private OnDrinkingListViewCallback mOnDrinkingListViewCallback;
    private CupListAdapter.OnCupItemClickCallback mOnCupItemClickCallback;
    private CupListAdapter mCupListAdapter;
    private ArrayList<CupListRsp.CupBean> cupBeanList;
    private CupListRsp.CupBean currentConnectCup;

    public void setOnDrinkingListViewCallback(OnDrinkingListViewCallback onDrinkingListViewCallback) {
        mOnDrinkingListViewCallback = onDrinkingListViewCallback;
    }

    public void setOnCupItemClickCallback(CupListAdapter.OnCupItemClickCallback onCupItemClickCallback) {
        mOnCupItemClickCallback = onCupItemClickCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.view_drinking_list, container, false);
        mBackBtn = mRootView.findViewById(R.id.iv_back);
        mCupListRv = mRootView.findViewById(R.id.rv_device_list);
        mAddDeviceBt = mRootView.findViewById(R.id.bt_add_device);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAddDeviceBt.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mCupListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mCupListAdapter = new CupListAdapter();
        mCupListAdapter.setOnCupItemClickCallback(mOnCupItemClickCallback);
        mCupListRv.setAdapter(mCupListAdapter);

        PlusItemSlideCallback callback = new PlusItemSlideCallback(WItemTouchHelperPlus.SLIDE_ITEM_TYPE_SLIDECONTAINER);
        WItemTouchHelperPlus extension = new WItemTouchHelperPlus(callback);
        extension.attachToRecyclerView(mCupListRv);
        setDatasUi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_device:
                ChooseAddDeviceWayFragment chooseAddDeviceWayFragment = new ChooseAddDeviceWayFragment();
                chooseAddDeviceWayFragment.show(getChildFragmentManager(), ChooseAddDeviceWayFragment.TAG);
                chooseAddDeviceWayFragment.setOnDrinkingListViewCallback(mOnDrinkingListViewCallback);
                break;
            case R.id.iv_back:
                mOnDrinkingListViewCallback.backToDrinkingStartView();
                break;
        }
    }

    public void notifyCupList(ArrayList<CupListRsp.CupBean> cupBeanList, CupListRsp.CupBean currentConnectCup) {
        this.cupBeanList = cupBeanList;
        this.currentConnectCup = currentConnectCup;
        setDatasUi();
    }

    private void setDatasUi() {
        if (mBackBtn != null) {
            mBackBtn.setVisibility(currentConnectCup != null ? View.VISIBLE : View.GONE);
        }
        if (mCupListAdapter != null && cupBeanList != null) {
            mCupListAdapter.setCupBeans(cupBeanList);
        }
    }


    public interface OnDrinkingListViewCallback {
        void backToDrinkingStartView();

        void onBluetoothDeviceFind(BluetoothDevice bluetoothDevice);

        void onWifiDeviceFind(WifiRsp wifiRsp);
    }
}
