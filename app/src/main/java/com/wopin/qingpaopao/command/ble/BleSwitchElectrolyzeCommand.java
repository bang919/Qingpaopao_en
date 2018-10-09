package com.wopin.qingpaopao.command.ble;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.command.ISwitchElectrolyzeCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleSwitchElectrolyzeCommand extends ISwitchElectrolyzeCommand<String> {

    public BleSwitchElectrolyzeCommand(String target, int time) {
        super(target, time);
    }

    @Override
    public void execute() {
        int time = getTime();
        String minute = "0" + Integer.toHexString(time / 60);
        String second = "0" + Integer.toHexString(time % 60);
        String s = time > 0 ? "AABBCC02" + minute.substring(minute.length() - 2) + second.substring(second.length() - 2) + "BBAA" : "AABBCC0102CCBBAA";
        LeProxy.getInstance().send(getTarget(), DataUtil.hexToByteArray(s));
    }
}
