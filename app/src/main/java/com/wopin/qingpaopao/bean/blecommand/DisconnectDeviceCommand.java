package com.wopin.qingpaopao.bean.blecommand;

import com.wopin.qingpaopao.utils.LeProxy;

public class DisconnectDeviceCommand implements ICommand {

    private String address;

    public DisconnectDeviceCommand(String address) {
        this.address = address;
    }

    @Override
    public void execute() {
        LeProxy.getInstance().disconnect(address);
    }
}
