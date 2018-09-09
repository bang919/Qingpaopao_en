package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.ISwitchCleanCommand;
import com.wopin.qingpaopao.manager.MqttConnectManager;

public class MqttSwitchCleanCommand extends ISwitchCleanCommand<String> {

    public MqttSwitchCleanCommand(String target, boolean clean) {
        super(target, clean);
    }

    @Override
    public void execute() {
        MqttConnectManager.getInstance().publish(getTarget(), isClean() ? "031" : "030");
    }
}
