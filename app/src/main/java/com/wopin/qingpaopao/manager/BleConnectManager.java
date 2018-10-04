package com.wopin.qingpaopao.manager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ble.ble.BleService;
import com.wopin.qingpaopao.command.ble.BleConnectDeviceCommand;
import com.wopin.qingpaopao.command.ble.BleDisconnectDeviceCommand;
import com.wopin.qingpaopao.command.ble.BleColorCommand;
import com.wopin.qingpaopao.command.ble.BleSwitchCleanCommand;
import com.wopin.qingpaopao.command.ble.BleSwitchElectrolyzeCommand;
import com.wopin.qingpaopao.command.ble.BleSwitchLightCommand;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleConnectManager extends ConnectManager<BleConnectManager.BleUpdaterBean> {

    private static final String TAG = "BleConnectManager";
    private static BleConnectManager mBlemanager;
    private ServiceConnection mConn;
    private BroadcastReceiver mReceiver;
    private boolean hadConnectOneDevice;

    private BleConnectManager() {

    }

    public static BleConnectManager getInstance() {
        if (mBlemanager == null) {
            mBlemanager = new BleConnectManager();
        }
        return mBlemanager;
    }

    @Override
    protected void connectToServer(final ConnectManager.OnServerConnectCallback onServerConnectCallback) {
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    BleUpdaterBean bleUpdaterBean = new BleUpdaterBean();
                    String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
                    bleUpdaterBean.setAddress(address);

                    switch (intent.getAction()) {
                        case LeProxy.ACTION_GATT_CONNECTED://连线
                            hadConnectOneDevice = true;
                            break;
                        case LeProxy.ACTION_GATT_DISCONNECTED:// 断线
                            onDissconnectDevice(bleUpdaterBean);
                            break;
                        case LeProxy.ACTION_RSSI_AVAILABLE: // 更新rssi
                            break;
                        case LeProxy.ACTION_DATA_AVAILABLE:// 接收到从机数据
                            String uuid = intent.getStringExtra(LeProxy.EXTRA_UUID);
                            byte[] values = intent.getByteArrayExtra(LeProxy.EXTRA_DATA);
                            bleUpdaterBean.setUuid(uuid);
                            bleUpdaterBean.setValues(values);
                            if (hadConnectOneDevice) {
                                hadConnectOneDevice = false;
                                onConnectDevice(bleUpdaterBean);
                            }
                            onDatasUpdate(bleUpdaterBean);
                            break;
                    }
                }
            };
            LocalBroadcastManager.getInstance(MyApplication.getMyApplicationContext()).registerReceiver(mReceiver, makeFilter());
        }


        if (mConn == null) {
            mConn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LeProxy.getInstance().setBleService(service);
                    LeProxy.getInstance().setEncrypt(true);
                    onServerConnectCallback.onConnectServerCallback();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    onServerConnectCallback.onDisconnectServerCallback();
                    Log.d(TAG, "onServiceDisconnected: BleManagerDisconnected");
                }
            };
            boolean success = MyApplication.getMyApplicationContext().bindService(new Intent(MyApplication.getMyApplicationContext(), BleService.class),
                    mConn, Context.BIND_AUTO_CREATE);
            if (!success) {
                onServerConnectCallback.onDisconnectServerCallback();
            }
        } else {
            onServerConnectCallback.onConnectServerCallback();
        }
    }

    private IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LeProxy.ACTION_GATT_CONNECTED);
        filter.addAction(LeProxy.ACTION_GATT_DISCONNECTED);
        filter.addAction(LeProxy.ACTION_RSSI_AVAILABLE);
        filter.addAction(LeProxy.ACTION_DATA_AVAILABLE);
        return filter;
    }

    @Override
    public void disconnectServer() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(MyApplication.getMyApplicationContext()).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        if (mConn != null) {
            try {

                MyApplication.getMyApplicationContext().unbindService(mConn);
                mConn = null;
            } catch (IllegalArgumentException e) {//可能会还没bindService
                Log.d(TAG, "disconnectServer: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * =============================================  Device =============================================
     */
    public void connectDevice(String address) {
        if (address != null) {
            super.connectDevice(new BleConnectDeviceCommand(address));
        }
    }

    public void disconnectDevice(String address) {
        super.disconnectDevice(new BleDisconnectDeviceCommand(address));
    }

    public void switchCupElectrolyze(String address, int time) {
        super.switchCupElectrolyze(new BleSwitchElectrolyzeCommand(address, time));
    }

    public void switchCupLight(String address, boolean isLightOn) {
        super.switchCupLight(new BleSwitchLightCommand(address, isLightOn));
    }

    public void switchCupClean(String address, boolean isClean) {
        super.switchCupClean(new BleSwitchCleanCommand(address, isClean));
    }

    public void setColor(String address, String color) {
        super.setColor(new BleColorCommand(address, color));
    }

    /**
     * =============================================  Updater =============================================
     */

    public class BleUpdaterBean {
        private String address;
        private String uuid;
        private byte[] values;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public byte[] getValues() {
            return values;
        }

        public void setValues(byte[] values) {
            this.values = values;
        }
    }
}
