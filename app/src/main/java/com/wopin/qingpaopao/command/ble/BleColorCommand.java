package com.wopin.qingpaopao.command.ble;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.command.IColorCommand;
import com.wopin.qingpaopao.utils.LeProxy;

public class BleColorCommand extends IColorCommand<String> {

    public BleColorCommand(String address, String color) {
        super(address, color);
    }

    @Override
    public void execute() {
        String colorString = getColor();

        String color = "AABBDD".concat(
                colorString.substring(4, 6)
                        .concat(colorString.substring(2, 4))
                        .concat(colorString.substring(0, 2))
        ).concat("DDAA");
        LeProxy.getInstance().send(getTarget(), DataUtil.hexToByteArray(color));
    }
}
