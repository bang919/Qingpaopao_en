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
import com.wopin.qingpaopao.command.ble.SwitchElectrolyzeCommand;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleManager extends ConnectManager<BleManager.BleUpdaterBean> {

    private static final String TAG = "BleManager";
    private static BleManager mBlemanager;
    private ServiceConnection mConn;
    private BroadcastReceiver mReceiver;
    private boolean hadConnectOneDevice;

    private BleManager() {

    }

    public static BleManager getInstance() {
        if (mBlemanager == null) {
            mBlemanager = new BleManager();
        }
        return mBlemanager;
    }

    @Override
    protected boolean connectToServer(final ConnectManager.OnServerConnectCallback onServerConnectCallback) {
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
        return MyApplication.getMyApplicationContext().bindService(new Intent(MyApplication.getMyApplicationContext(), BleService.class),
                mConn, Context.BIND_AUTO_CREATE);
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
        LocalBroadcastManager.getInstance(MyApplication.getMyApplicationContext()).unregisterReceiver(mReceiver);
        MyApplication.getMyApplicationContext().unbindService(mConn);
        clearUpdaters();
    }

    /**
     * =============================================  Device =============================================
     */
    public void connectDevice(String address) {
        super.connectDevice(new BleConnectDeviceCommand(address));
    }

    public void disconnectDevice(String address) {
        super.disconnectDevice(new BleDisconnectDeviceCommand(address));
    }

    public void switchCupElectrolyze(String address, boolean isOn) {
        super.switchCupElectrolyze(new SwitchElectrolyzeCommand(address, isOn));
    }

    /**
     * =============================================  Updater =============================================
     */
    public void addUpdater(Updater<BleUpdaterBean> updater) {
        super.addUpdater(updater);
    }

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
