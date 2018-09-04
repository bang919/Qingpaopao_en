package com.wopin.qingpaopao.command.ble;

import com.wopin.qingpaopao.command.IConnectDeviceCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleConnectDeviceCommand extends IConnectDeviceCommand<String> {

    public BleConnectDeviceCommand(String address) {
        super(address);
    }

    @Override
    public void execute() {
        LeProxy.getInstance().connect(getTarget(), true);
    }
}
