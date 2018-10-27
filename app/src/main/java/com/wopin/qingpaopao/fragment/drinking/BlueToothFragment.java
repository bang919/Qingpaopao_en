package com.wopin.qingpaopao.fragment.drinking;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.adapter.BleChooseListRvAdapter;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.BlueToothPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;

import java.util.TreeMap;

public class BlueToothFragment extends BaseBarDialogFragment implements BlueToothPresenter.BlueToothPresenterCallback, View.OnClickListener {

    public static final String TAG = "BlueToothFragment";
    private BlueToothPresenter mBlueToothPresenter;
    private ImageView mCircleIv;
    private OnDeviceClickListener mOnDeviceClickListener;
    private RecyclerView mBleRv;
    private BleChooseListRvAdapter mBleChooseListRvAdapter;

    public void setOnDeviceClickListener(OnDeviceClickListener onDeviceClickListener) {
        mOnDeviceClickListener = onDeviceClickListener;
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    public void destroy() {
        mBlueToothPresenter.destroy();
        mCircleIv.clearAnimation();
    }

    @Override
    protected String setBarTitle() {
        return getString(R.string.bluetooth_version);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_bluetooth;
    }

    @Override
    protected BasePresenter initPresenter() {
        mBlueToothPresenter = new BlueToothPresenter(this, this);
        return null;
    }

    @Override
    protected void initView(View rootView) {
        mCircleIv = rootView.findViewById(R.id.iv_circle);
        mBleRv = rootView.findViewById(R.id.rv_ble_list);
        mBleRv.setLayoutManager(new LinearLayoutManager(getContext()));

        rootView.findViewById(R.id.cant_find_your_cup).setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        ScaleAnimation animation = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        mCircleIv.startAnimation(animation);

        mBlueToothPresenter.start();

        mBleChooseListRvAdapter = new BleChooseListRvAdapter(new BleChooseListRvAdapter.BleChooseListAdapterCallback() {
            @Override
            public void onBleDeviceChoose(BluetoothDevice bluetoothDevice, int position) {
                destroy();
                dismiss();
                mOnDeviceClickListener.onBlueToothDeviceClick(bluetoothDevice, position);
            }
        });
        mBleRv.setAdapter(mBleChooseListRvAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBlueToothPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDevicesFind(TreeMap<String, BluetoothDevice> devices, BluetoothDevice newDevice) {
        mBleChooseListRvAdapter.putBlueToothDevice(newDevice);
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cant_find_your_cup:
                //重新搜索
                mBleChooseListRvAdapter.putBlueToothDevice(null);
                mBlueToothPresenter.start();
                break;
        }
    }

    public interface OnDeviceClickListener {
        void onBlueToothDeviceClick(BluetoothDevice bluetoothDevice, int position);
    }
}
