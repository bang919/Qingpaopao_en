package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.IConnectDeviceCommand;
import com.wopin.qingpaopao.manager.MqttConnectManager;

public class MqttConnectDeviceCommand extends IConnectDeviceCommand<String> {

    public MqttConnectDeviceCommand(String target) {
        super(target);
    }

    @Override
    public void execute() {
        MqttConnectManager.getInstance().subscribe(getTarget());
    }
}
