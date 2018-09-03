package com.wopin.qingpaopao.bean.blecommand;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.utils.LeProxy;

public class SwitchElectrolyzeCommand implements ICommand {

    private String address;
    private boolean electrolyze;

    public SwitchElectrolyzeCommand(String address, boolean electrolyze) {
        this.address = address;
        this.electrolyze = electrolyze;
    }

    @Override
    public void execute() {
        String s = electrolyze ? "AABBCC0101CCBBAA" : "AABBCC0102CCBBAA";
        LeProxy.getInstance().queueSend(address, DataUtil.hexToByteArray(s));
    }
}
