package com.wopin.qingpaopao.presenter;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wopin.qingpaopao.R;
import com.wopin.qingpaopao.utils.ToastUtils;

import java.util.TreeMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BlueToothPresenter {

    private static final int BLUETOOTH_OPEN_REQUEST_CODE = 0x142;
    private BlueToothPresenterCallback mBlueToothPresenterCallback;
    private FragmentActivity mFragmentActivity;
    private Fragment mFragment;
    private Context mContext;
    private final BluetoothManager mBluetoothManager;
    private final BluetoothAdapter mBluetoothManagerAdapter;
    private TreeMap<String, BluetoothDevice> mBluetoothDevices;

    public <T> BlueToothPresenter(T t, BlueToothPresenterCallback blueToothPresenterCallback) {
        mBlueToothPresenterCallback = blueToothPresenterCallback;
        if (t instanceof FragmentActivity) {
            mFragmentActivity = (FragmentActivity) t;
            mContext = mFragmentActivity;
        } else if (t instanceof Fragment) {
            mFragment = (Fragment) t;
            mContext = mFragment.getContext();
        }
        mBluetoothDevices = new TreeMap<>();
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothManagerAdapter = mBluetoothManager.getAdapter();
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        mContext.registerReceiver(searchDevices, intent);
    }

    /**
     * 反注册广播取消蓝牙的配对
     */
    public void destroy() {
        mContext.unregisterReceiver(searchDevices);
        stopSearthBltDevice();
    }

    /**
     * 蓝牙接收广播
     */
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        //接收
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
            }
            BluetoothDevice device;
            // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String address = device.getAddress();
                if (mBluetoothDevices.get(address) == null) {
                    mBluetoothDevices.put(address, device);
                    mBlueToothPresenterCallback.onDevicesFind(mBluetoothDevices, device);
                }
            }
        }
    };

    /**
     * 判断是否支持蓝牙，并打开蓝牙
     * 获取到BluetoothAdapter之后，还需要判断是否支持蓝牙，以及蓝牙是否打开。
     * 如果没打开，需要让用户打开蓝牙：
     */
    private void checkBleDevice() {
        if (mBluetoothManagerAdapter != null) {
            if (!mBluetoothManagerAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (mFragmentActivity != null) {
                    mFragmentActivity.startActivityForResult(enableBtIntent, BLUETOOTH_OPEN_REQUEST_CODE);
                } else if (mFragment != null) {
                    mFragment.startActivityForResult(enableBtIntent, BLUETOOTH_OPEN_REQUEST_CODE);
                }
            } else {
                startSearthBltDevice();
            }
        } else {
            mBlueToothPresenterCallback.onError(mContext.getString(R.string.not_support_bluetooth));
        }
    }

    public void start() {
        RxPermissions rxPermissions = null;
        if (mFragmentActivity != null) {
            rxPermissions = new RxPermissions(mFragmentActivity);
        } else if (mFragment != null) {
            rxPermissions = new RxPermissions(mFragment);
        }
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            checkBleDevice();
                        } else {
                            mBlueToothPresenterCallback.onError(mContext.getString(R.string.need_permission));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBlueToothPresenterCallback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BLUETOOTH_OPEN_REQUEST_CODE && resultCode != Activity.RESULT_CANCELED) {
            startSearthBltDevice();
        }
    }

    private void startSearthBltDevice() {
        //开启位置
        LocationManager m_location_manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showShort("Neet Location permission.");
            return;
        }
        Location lm = m_location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //开始搜索设备，当搜索到一个设备的时候就应该将它添加到设备集合中，保存起来
        //如果当前发现了新的设备，则停止继续扫描，当前扫描到的新设备会通过广播推向新的逻辑
        if (mBluetoothManagerAdapter.isDiscovering())
            stopSearthBltDevice();
        //开始搜索
        mBluetoothManagerAdapter.startDiscovery();
        //这里的true并不是代表搜索到了设备，而是表示搜索成功开始。
    }

    private void stopSearthBltDevice() {
        //暂停搜索设备
        if (mBluetoothManagerAdapter != null)
            mBluetoothManagerAdapter.cancelDiscovery();
    }

    public interface BlueToothPresenterCallback {
        void onDevicesFind(TreeMap<String, BluetoothDevice> devices, BluetoothDevice newDevice);

        void onError(String errorMsg);
    }


}