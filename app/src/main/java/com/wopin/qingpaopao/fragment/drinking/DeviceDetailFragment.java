package com.wopin.qingpaopao.fragment.drinking;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.bean.response.CupListRsp;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.manager.BleManager;
import com.wopin.qingpaopao.manager.Updater;
import com.wopin.qingpaopao.presenter.BasePresenter;

public class DeviceDetailFragment extends BaseBarDialogFragment implements View.OnClickListener {

    public static final String TAG = "DeviceDetailFragment";
    private CupListRsp.CupBean mCupBean;
    private TextView mElectricTv;
    private TextView mDeviceNameTv;
    private TextView mCupColorTv;
    private TextView mDeviceStatusTv;
    private BleManager mBleManager;
    private Updater<BleManager.BleUpdaterBean> mUpdater;

    public static DeviceDetailFragment getDeviceDetailFragment(CupListRsp.CupBean cupBean) {
        DeviceDetailFragment deviceDetailFragment = new DeviceDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAG, cupBean);
        deviceDetailFragment.setArguments(args);
        return deviceDetailFragment;
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.device_manager);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_device_manager;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mCupBean = getArguments().getParcelable(TAG);
        mElectricTv = rootView.findViewById(R.id.value_electric);
        mDeviceNameTv = rootView.findViewById(R.id.value_device_name);
        mCupColorTv = rootView.findViewById(R.id.value_cup_color);
        mDeviceStatusTv = rootView.findViewById(R.id.value_device_status);
    }

    @Override
    protected void initEvent() {
        mDeviceNameTv.setText(mCupBean.getName());
        mDeviceNameTv.setOnClickListener(this);
        if (mCupBean.isConnecting()) {
            mDeviceStatusTv.setText(R.string.bind);
            mDeviceStatusTv.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            mElectricTv.setText(TextUtils.isEmpty(mCupBean.getElectric()) ? "0%" : mCupBean.getElectric() + "%");

            mBleManager = BleManager.getInstance();
            mUpdater = new Updater<BleManager.BleUpdaterBean>() {
                @Override
                public void onDatasUpdate(BleManager.BleUpdaterBean bleUpdaterBean) {
                    byte[] values = bleUpdaterBean.getValues();
                    String s = DataUtil.byteArrayToHex(values);
                    parseData(s);
                }
            };
            mBleManager.addUpdater(mUpdater);
        }
    }

    private void parseData(String data) {
        if (data.startsWith("AA CC DD 01 ") && data.endsWith(" DD CC AA")) {//电量数据
            String hexElectric = data.replaceFirst("AA CC DD 01 ", "").replace(" DD CC AA", "");
            mElectricTv.setText(Integer.valueOf(hexElectric, 16) + "%");
        }
    }

    @Override
    public void onDestroy() {
        if (mBleManager != null) {
            mBleManager.removeUpdater(mUpdater);
            mUpdater = null;
            mBleManager = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.value_device_name:
//                BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
//                BluetoothAdapter adapter = bluetoothManager.getAdapter();
//                adapter.setName("BigBang");
                break;
        }
    }
}
