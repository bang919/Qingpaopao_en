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
        String timeString = String.format("%05X", time);
        MqttConnectManager.getInstance().publish(getTarget(), time > 0 ? "021" + timeString : "02000000");
    }
}
