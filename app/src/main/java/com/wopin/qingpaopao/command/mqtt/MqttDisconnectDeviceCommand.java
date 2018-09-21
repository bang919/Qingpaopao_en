package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.IDisconnectDeviceCommand;
import com.wopin.qingpaopao.manager.MqttConnectManager;

public class MqttDisconnectDeviceCommand extends IDisconnectDeviceCommand<String> {

    public MqttDisconnectDeviceCommand(String target) {
        super(target);
    }

    @Override
    public void execute() {
        MqttConnectManager.getInstance().unsubscribe(getTarget());
    }
}
