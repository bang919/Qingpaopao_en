package com.wopin.qingpaopao.manager;

import com.ble.api.DataUtil;

import java.util.TreeMap;

/**
 * 用来统一管理Ble和Mqtt的message返回
 */
public class MessageProxy {

    private static TreeMap<String, MessageProxyCallback> callbackTree;
    private Updater<BleConnectManager.BleUpdaterBean> mUpdaterBle;
    private Updater<MqttConnectManager.MqttUpdaterBean> mUpdaterMqtt;

    public MessageProxy() {
        callbackTree = new TreeMap<>();
    }

    public void startListening() {
        mUpdaterBle = new Updater<BleConnectManager.BleUpdaterBean>() {
            @Override
            public void onDatasUpdate(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                parseBleMessage(bleUpdaterBean);
            }
        };
        BleConnectManager.getInstance().addUpdater(mUpdaterBle);

        mUpdaterMqtt = new Updater<MqttConnectManager.MqttUpdaterBean>() {
            @Override
            public void onDatasUpdate(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                parseMqttMessage(mqttUpdaterBean);
            }
        };
        MqttConnectManager.getInstance().addUpdater(mUpdaterMqtt);
    }

    public void release() {
        BleConnectManager.getInstance().removeUpdater(mUpdaterBle);
        MqttConnectManager.getInstance().removeUpdater(mUpdaterMqtt);
        BleConnectManager.getInstance().destroy();
        MqttConnectManager.getInstance().destroy();
        WifiConnectManager.getInstance().destroy();
    }

    /**
     * 解析ble
     */
    private void parseBleMessage(BleConnectManager.BleUpdaterBean bleUpdaterBean) {
        byte[] values = bleUpdaterBean.getValues();
        String s = DataUtil.byteArrayToHex(values);
        parseBleData(bleUpdaterBean.getUuid(), s);
    }


    private void parseBleData(String uuid, String data) {
        if (data.length() == 47) {
            String data1 = data.substring(0, 23);
            String data2 = data.substring(24, 47);
            parseBleData(uuid, data1);
            parseBleData(uuid, data2);
            return;
        }
        MessageProxyCallback messageProxyCallback = callbackTree.get(uuid);
        if (messageProxyCallback != null) {
            if (data.startsWith("AA CC DD 01 ") && data.endsWith(" DD CC AA")) {//电量数据
                String hexElectric = data.replaceFirst("AA CC DD 01 ", "").replace(" DD CC AA", "");
                Integer integerElectric = Integer.valueOf(hexElectric, 16);
                messageProxyCallback.onBattery(uuid, integerElectric);
            } else if (data.startsWith("AA CC DD 05 ") && data.endsWith(" CC AA")) {//电解/冲洗时间
                String[] split = data.replaceFirst("AA CC DD 05 ", "").replace(" CC AA", "").split(" ");
                String m = "0" + Integer.valueOf(split[0], 16);
                String s = "0" + Integer.valueOf(split[1], 16);
                messageProxyCallback.onTime(uuid, m.substring(m.length() - 2), s.substring(s.length() - 2));
            } else if (data.equals("AA CC DD 03 01 DD CC AA")) {//电解中
                messageProxyCallback.onElectrolyzing(uuid);
            } else if (data.equals("AA CC DD 03 02 DD CC AA")) {//电解结束
                messageProxyCallback.onElectrolyzeEnd(uuid);
            } else if (data.startsWith("AA CC DD 03 03 DD CC AA")) {//冲洗中
                messageProxyCallback.onCleaning(uuid);
            } else if (data.equals("AA CC DD 03 04 DD CC AA")) {//冲洗结束
                messageProxyCallback.onCleaneEnd(uuid);
            }
        }
    }

    /**
     * 解析Mqtt
     */
    private void parseMqttMessage(MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
        String uuid = mqttUpdaterBean.getSsid();
        String message = mqttUpdaterBean.getMessage();
        String[] split = message.split(":");
        MessageProxyCallback messageProxyCallback = callbackTree.get(uuid);
        if (messageProxyCallback != null) {
            if (split[0].equals("P")) {//电量数据
                messageProxyCallback.onBattery(uuid, Integer.valueOf(split[1].substring(0, 2)));

            } else if (split[2].equals("H")) {
                //Update hydro timer  --> if split[4] == M and split[5] == 0
                //Update clean timer --> if split[4] == M and split[5] == 1
                //If hydro timer == 0 and split[5] == 0 --> Hydro Finish
                //If hydro timer == 0 and split[5] == 1 --> Clean Finish
            }
        }
    }

    public static void addMessageProxyCallback(String uuid, MessageProxyCallback messageProxyCallback) {
        callbackTree.put(uuid, messageProxyCallback);
    }

    public static void removeMessageProxyCallback(String uuid) {
        callbackTree.remove(uuid);
    }

    public static void clearMessageProxyCallback() {
        callbackTree.clear();
    }
}
