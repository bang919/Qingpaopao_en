package com.wopin.qingpaopao.command.ble;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.command.IColorCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class ColorCommand extends IColorCommand<String> {

    public ColorCommand(String address, String color) {
        super(address, color);
    }

    @Override
    public void execute() {
        String s = "AABBDD".concat(getColor()).concat("DDAA");
        LeProxy.getInstance().send(getTarget(), DataUtil.hexToByteArray(s));
    }
}
