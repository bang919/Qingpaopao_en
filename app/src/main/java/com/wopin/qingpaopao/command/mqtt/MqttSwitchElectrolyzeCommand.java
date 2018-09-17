package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.ISwitchElectrolyzeCommand;
import com.wopin.qingpaopao.manager.MqttConnectManager;

public class MqttSwitchElectrolyzeCommand extends ISwitchElectrolyzeCommand<String> {

    public MqttSwitchElectrolyzeCommand(String target, int time) {
        super(target, time);
    }

    @Override
    public void execute() {
        int time = getTime();
        MqttConnectManager.getInstance().publish(getTarget(), time > 0 ? "021" + time : "0200000");
    }
}
