package com.wopin.qingpaopao.command.ble;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.command.ISwitchElectrolyzeCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class SwitchElectrolyzeCommand extends ISwitchElectrolyzeCommand<String> {

    public SwitchElectrolyzeCommand(String target, boolean electrolyze) {
        super(target, electrolyze);
    }

    @Override
    public void execute() {
        String s = isElectrolyze() ? "AABBCC0101CCBBAA" : "AABBCC0102CCBBAA";
        LeProxy.getInstance().send(getTarget(), DataUtil.hexToByteArray(s));
    }
}
