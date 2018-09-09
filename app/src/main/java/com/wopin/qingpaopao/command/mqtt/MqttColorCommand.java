package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.IColorCommand;
import com.wopin.qingpaopao.manager.MqttConnectManager;

public class MqttColorCommand extends IColorCommand<String> {

    public MqttColorCommand(String target, String color) {
        super(target, color);
    }

    @Override
    public void execute() {
        MqttConnectManager.getInstance().publish(getTarget(), "01".concat(getColor()));
    }
}
