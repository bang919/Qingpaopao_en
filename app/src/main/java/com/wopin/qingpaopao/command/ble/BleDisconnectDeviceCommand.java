package com.wopin.qingpaopao.command.ble;

import com.wopin.qingpaopao.command.IDisconnectDeviceCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleDisconnectDeviceCommand extends IDisconnectDeviceCommand<String> {

    public BleDisconnectDeviceCommand(String target) {
        super(target);
    }

    @Override
    public void execute() {
        LeProxy.getInstance().disconnect(getTarget());
    }
}
