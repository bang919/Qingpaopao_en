package com.wopin.qingpaopao.fragment.drinking;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.fragment.BaseBarDialogFragment;
import com.wopin.qingpaopao.presenter.BasePresenter;
import com.wopin.qingpaopao.presenter.BlueToothPresenter;
import com.wopin.qingpaopao.utils.ToastUtils;
import com.wopin.qingpaopao.widget.RandomTextLayout;

import java.util.TreeMap;

public class BlueToothFragment extends BaseBarDialogFragment implements BlueToothPresenter.BlueToothPresenterCallback {

    public static final String TAG = "BlueToothFragment";
    private BlueToothPresenter mBlueToothPresenter;
    private ImageView mCircleIv;
    private RandomTextLayout mRandomTextLayout;
    private RandomTextLayout.OnDeviceClickListener mOnDeviceClickListener;

    public void setOnDeviceClickListener(RandomTextLayout.OnDeviceClickListener onDeviceClickListener) {
        mOnDeviceClickListener = onDeviceClickListener;
    }

    @Override
    public void onDestroy() {
        mBlueToothPresenter.destroy();
        mCircleIv.clearAnimation();
        super.onDestroy();
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
        mRandomTextLayout = rootView.findViewById(R.id.random_text_layout);
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
        mRandomTextLayout.setOnDeviceClickListener(new RandomTextLayout.OnDeviceClickListener() {
            @Override
            public void onBlueToothDeviceClick(BluetoothDevice bluetoothDevice, int position) {
                dismiss();
                mOnDeviceClickListener.onBlueToothDeviceClick(bluetoothDevice, position);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBlueToothPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDevicesFind(TreeMap<String, BluetoothDevice> devices, BluetoothDevice newDevice) {
        mRandomTextLayout.addBluetoothDevice(newDevice);
    }

    @Override
    public void onError(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }
}
