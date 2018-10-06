package com.wopin.qingpaopao.manager;

public abstract class MessageProxyCallback {
    //电池电量数据
    public void onBattery(String uuid, int battery) {
    }

    //电解/冲洗时间
    public void onTime(String uuid, String minute, String second) {
    }

    //电解中
    public void onElectrolyzing(String uuid) {

    }

    //电解结束
    public void onElectrolyzeEnd(String uuid) {
    }


    //冲洗中
    public void onCleaning(String uuid) {

    }

    //冲洗结束
    public void onCleaneEnd(String uuid) {
    }
}
