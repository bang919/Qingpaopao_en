package com.wopin.qingpaopao.bean.blecommand;

import android.bluetooth.BluetoothDevice;

import com.wopin.qingpaopao.utils.LeProxy;

public class ConnectDeviceCommand implements ICommand {

    private BluetoothDevice mBluetoothDevice;

    public ConnectDeviceCommand(BluetoothDevice bluetoothDevice) {
        mBluetoothDevice = bluetoothDevice;
    }

    @Override
    public void execute() {
        LeProxy.getInstance().connect(mBluetoothDevice.getAddress(), false);
    }
}
