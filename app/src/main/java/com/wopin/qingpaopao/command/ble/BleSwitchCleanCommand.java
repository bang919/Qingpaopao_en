package com.wopin.qingpaopao.command.ble;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.command.ISwitchCleanCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleSwitchCleanCommand extends ISwitchCleanCommand<String> {

    public BleSwitchCleanCommand(String target, boolean clean) {
        super(target, clean);
    }

    @Override
    public void execute() {
        String s = isClean() ? "AABBCC0103CCBBAA" : "AABBCC0104CCBBAA";
        LeProxy.getInstance().send(getTarget(), DataUtil.hexToByteArray(s));
    }
}
