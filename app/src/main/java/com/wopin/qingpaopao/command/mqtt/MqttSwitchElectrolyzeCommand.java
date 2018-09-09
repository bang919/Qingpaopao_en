package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.ISwitchElectrolyzeCommand;
import com.wopin.qingpaopao.manager.MqttConnectManager;

public class MqttSwitchElectrolyzeCommand extends ISwitchElectrolyzeCommand<String> {

    public MqttSwitchElectrolyzeCommand(String target, boolean electrolyze) {
        super(target, electrolyze);
    }

    @Override
    public void execute() {
        MqttConnectManager.getInstance().publish(getTarget(), isElectrolyze() ? "0219999" : "0200000");
    }
}
