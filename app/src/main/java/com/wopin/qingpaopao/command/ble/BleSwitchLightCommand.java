package com.wopin.qingpaopao.command.ble;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.command.ISwitchLightCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleSwitchLightCommand extends ISwitchLightCommand<String> {

    public BleSwitchLightCommand(String target, boolean isLightOn) {
        super(target, isLightOn);
    }

    @Override
    public void execute() {
        String s = isLightOn() ? "AABBCC0301CCBBAA" : "AABBCC0302CCBBAA";
        LeProxy.getInstance().send(getTarget(), DataUtil.hexToByteArray(s));
    }
}
