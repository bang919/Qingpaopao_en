package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.ISwitchLightCommand;
import com.wopin.qingpaopao.manager.MqttConnectManager;

public class MqttSwitchLightCommand extends ISwitchLightCommand<String> {

    public MqttSwitchLightCommand(String target, boolean isLightOn) {
        super(target, isLightOn);
    }

    @Override
    public void execute() {
        MqttConnectManager.getInstance().publish(getTarget(), isLightOn() ? "041" : "040");
    }
}
