package com.wopin.qingpaopao.fragment.drinking;

import android.bluetooth.BluetoothDevice;
import android.view.View;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.widget.RandomTextLayout;

public class ChooseAddDeviceWayFragment extends BaseBarDialogFragment implements View.OnClickListener, RandomTextLayout.OnDeviceClickListener {

    public static final String TAG = "ChooseAddDeviceWayFragment";
    private DrinkingListView.OnDrinkingListViewCallback mOnDrinkingListViewCallback;

    public void setOnDrinkingListViewCallback(DrinkingListView.OnDrinkingListViewCallback onDrinkingListViewCallback) {
        mOnDrinkingListViewCallback = onDrinkingListViewCallback;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.add_device);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_choose_add_device_way;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.add_bluetooth_layout).setOnClickListener(this);
        rootView.findViewById(R.id.add_wifi_layout).setOnClickListener(this);
        rootView.findViewById(R.id.dont_know_version).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bluetooth_layout:
                BlueToothFragment blueToothFragment = new BlueToothFragment();
                blueToothFragment.setOnDeviceClickListener(this);
                blueToothFragment.show(getChildFragmentManager(), BlueToothFragment.TAG);
                break;
            case R.id.add_wifi_layout:
                break;
            case R.id.dont_know_version:
                new DontKnowVersionFragment().show(getChildFragmentManager(), DontKnowVersionFragment.TAG);
                break;
        }
    }

    @Override
    public void onBlueToothDeviceClick(BluetoothDevice bluetoothDevice, int position) {
        if (mOnDrinkingListViewCallback != null) {
            mOnDrinkingListViewCallback.onBluetoothDeviceFind(bluetoothDevice);
            dismiss();
        }
    }

}
