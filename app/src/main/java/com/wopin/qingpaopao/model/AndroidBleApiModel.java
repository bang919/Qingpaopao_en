package com.wopin.qingpaopao.model;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.ble.ble.BleService;
import com.wopin.qingpaopao.command.ble.BleConnectDeviceCommand;
import com.wopin.qingpaopao.command.ble.BleDisconnectDeviceCommand;
import com.wopin.qingpaopao.command.ICommand;
import com.wopin.qingpaopao.command.ble.SwitchElectrolyzeCommand;
import com.wopin.qingpaopao.utils.LeProxy;

import java.util.ArrayList;

public class AndroidBleApiModel {
    private Context mContext;
    private boolean isBleServerConncted;
    private ArrayList<ICommand> mICommands;
    private BluetoothDevice mCurrentBluetoothDevice;
    private String mCurrentUuid;
    private AndroidBleApiModelCallback mAndroidBleApiModelCallback;
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            destroy();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LeProxy.getInstance().setBleService(service);
            LeProxy.getInstance().setEncrypt(true);
            execute();
        }
    };

    private final BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);

            switch (intent.getAction()) {
                case LeProxy.ACTION_GATT_CONNECTED://连线
                    break;
                case LeProxy.ACTION_GATT_DISCONNECTED:// 断线
                    if (mCurrentUuid != null && mAndroidBleApiModelCallback != null) {
                        mAndroidBleApiModelCallback.onDisConnectedBluetoothDevice(mCurrentUuid);
                        mCurrentUuid = null;
                    }
                    break;
                case LeProxy.ACTION_RSSI_AVAILABLE: // 更新rssi

                    break;
                case LeProxy.ACTION_DATA_AVAILABLE:// 接收到从机数据
                    String uuid = intent.getStringExtra(LeProxy.EXTRA_UUID);
                    byte[] values = intent.getByteArrayExtra(LeProxy.EXTRA_DATA);
                    if (mCurrentBluetoothDevice != null && mCurrentUuid == null && mAndroidBleApiModelCallback != null) {
                        mAndroidBleApiModelCallback.onConnectedBluetoothDevice(mCurrentBluetoothDevice, uuid);
                        mCurrentUuid = uuid;
                    }
                    break;
            }
        }
    };

    public AndroidBleApiModel(Context context, AndroidBleApiModelCallback androidBleApiModelCallback) {
        mContext = context;
        mAndroidBleApiModelCallback = androidBleApiModelCallback;
        mICommands = new ArrayList<>();
    }

    public void destroy() {
        if (isBleServerConncted) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mLocalReceiver);
            mContext.unbindService(mConnection);
            isBleServerConncted = false;
        }
    }

    /**
     * 连接设备
     */
    public void connect(BluetoothDevice bluetoothDevice) {
        mCurrentBluetoothDevice = bluetoothDevice;
        addCommand(new BleConnectDeviceCommand(bluetoothDevice.getAddress()));
    }

    /**
     * 断开连接设备
     */
    public void disconnectCurrentBleDevice() {
        if (mCurrentBluetoothDevice != null) {
            String address = mCurrentBluetoothDevice.getAddress();
            mCurrentBluetoothDevice = null;
            addCommand(new BleDisconnectDeviceCommand(address));
        }
    }

    /**
     * 开/关杯子电解
     */
    public void switchCupElectrolyze(boolean electrolyze) {
        if (mCurrentBluetoothDevice != null) {
            String address = mCurrentBluetoothDevice.getAddress();
            addCommand(new SwitchElectrolyzeCommand(address, electrolyze));
        }
    }

    private void addCommand(ICommand iCommand) {
        mICommands.add(iCommand);
        //Check Connect
        if (!isBleServerConncted) {
            LocalBroadcastManager.getInstance(mContext).registerReceiver(mLocalReceiver, makeFilter());
            isBleServerConncted = mContext.bindService(new Intent(mContext, BleService.class), mConnection, Context.BIND_AUTO_CREATE);
        } else {
            execute();
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

    private void execute() {
        while (isBleServerConncted && mICommands.size() > 0) {
            ICommand remove = mICommands.remove(0);
            remove.execute();
        }
    }

    public interface AndroidBleApiModelCallback {
        void onConnectedBluetoothDevice(BluetoothDevice bluetoothDevice, String uuid);

        void onDisConnectedBluetoothDevice(String uuid);
    }
}
