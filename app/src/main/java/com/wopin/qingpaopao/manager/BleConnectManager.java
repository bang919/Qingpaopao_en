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

import com.ble.api.DataUtil;
import com.ble.ble.BleService;
import com.wopin.qingpaopao.command.ble.BleColorCommand;
import com.wopin.qingpaopao.command.ble.BleConnectDeviceCommand;
import com.wopin.qingpaopao.command.ble.BleDisconnectDeviceCommand;
import com.wopin.qingpaopao.command.ble.BleSwitchCleanCommand;
import com.wopin.qingpaopao.command.ble.BleSwitchElectrolyzeCommand;
import com.wopin.qingpaopao.command.ble.BleSwitchLightCommand;
import com.wopin.qingpaopao.common.MyApplication;
import com.wopin.qingpaopao.model.DrinkingModel;
import com.wopin.qingpaopao.utils.HttpUtil;
import com.wopin.qingpaopao.utils.LeProxy;

import java.util.Map;
import java.util.TreeMap;

public class BleConnectManager extends ConnectManager<BleConnectManager.BleUpdaterBean> {

    private static final String TAG = "BleConnectManager";
    private static BleConnectManager mBlemanager;
    private ServiceConnection mConn;
    private BroadcastReceiver mReceiver;
    private boolean hadConnectOneDevice;

    private TreeMap<String, BleUpdaterBean> mOnlineBleBeans;//key address , value BleUpdaterBean (有uuid）

    private BleConnectManager() {
        mOnlineBleBeans = new TreeMap<>();
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
                            checkElectrolyTimeToDrink(address);
                            if (mOnlineBleBeans.get(address) != null) {
                                onDissconnectDevice(bleUpdaterBean);
                                mOnlineBleBeans.remove(address);
                            }
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
                                mOnlineBleBeans.put(address, bleUpdaterBean);
                                onConnectDevice(bleUpdaterBean);
                            }

                            checkElectrolyTime(address, DataUtil.byteArrayToHex(values));

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

    /**
     * 用于计算电解时间，如果大于5分钟即请求drink API，计算一次喝水
     */
    private void checkElectrolyTime(String address, String data) {
        if (data.length() == 47) {
            String data1 = data.substring(0, 23);
            String data2 = data.substring(24, 47);
            checkElectrolyTime(address, data1);
            checkElectrolyTime(address, data2);
            return;
        }
        if (data.startsWith("AA CC DD 03 01")) {//电解中
            BleUpdaterBean bleUpdaterBean = mOnlineBleBeans.get(address);
            if (bleUpdaterBean.getBleStartElectrolyTime() == 0) {
                bleUpdaterBean.setBleStartElectrolyTime(System.currentTimeMillis());
            }
        } else if (data.equals("AA CC DD 03 02 DD CC AA")) {//电解结束
            //大于5分钟才请求drink，但是考虑到可能连通callback需要时间，所以取了4.5分钟
            checkElectrolyTimeToDrink(address);
        }
    }

    private void checkElectrolyTimeToDrink(String address) {
        BleUpdaterBean bleUpdaterBean = mOnlineBleBeans.get(address);
        long bleStartElectrolyTime = bleUpdaterBean.getBleStartElectrolyTime();
        if (bleStartElectrolyTime != 0 && System.currentTimeMillis() - bleStartElectrolyTime > 4.5 * 60 * 1000) {
            HttpUtil.subscribeNetworkTask(new DrinkingModel().drink(bleUpdaterBean.getUuid()), null);//喝水
        }
        bleUpdaterBean.setBleStartElectrolyTime(0);
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
        for (Map.Entry<String, BleUpdaterBean> next : mOnlineBleBeans.entrySet()) {
            onDissconnectDevice(next.getValue());
        }
        mOnlineBleBeans.clear();
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
        private long bleStartElectrolyTime;//用于计算电解时间，如果大于5分钟即请求drink API，计算一次喝水

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

        private long getBleStartElectrolyTime() {
            return bleStartElectrolyTime;
        }

        private void setBleStartElectrolyTime(long bleStartElectrolyTime) {
            this.bleStartElectrolyTime = bleStartElectrolyTime;
        }
    }
}
