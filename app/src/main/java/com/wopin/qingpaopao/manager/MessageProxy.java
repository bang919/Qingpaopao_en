package com.wopin.qingpaopao.manager;

import android.os.Handler;
import android.os.Looper;

import com.ble.api.DataUtil;
import com.wopin.qingpaopao.model.DrinkingModel;
import com.wopin.qingpaopao.utils.HttpUtil;

import java.util.TreeMap;

/**
 * 用来统一管理Ble和Mqtt的message返回
 */
public class MessageProxy {

    private static TreeMap<String, MessageProxyCallback> callbackTree;
    private Updater<BleConnectManager.BleUpdaterBean> mUpdaterBle;
    private Updater<MqttConnectManager.MqttUpdaterBean> mUpdaterMqtt;
    private Handler mainHandler;

    public MessageProxy() {
        callbackTree = new TreeMap<>();
    }

    public void startListening() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        mUpdaterBle = new Updater<BleConnectManager.BleUpdaterBean>() {
            @Override
            public void onDatasUpdate(final BleConnectManager.BleUpdaterBean bleUpdaterBean) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        parseBleMessage(bleUpdaterBean);
                    }
                });
            }
        };
        BleConnectManager.getInstance().addUpdater(mUpdaterBle);

        mUpdaterMqtt = new Updater<MqttConnectManager.MqttUpdaterBean>() {
            @Override
            public void onDatasUpdate(final MqttConnectManager.MqttUpdaterBean mqttUpdaterBean) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        parseMqttMessage(mqttUpdaterBean);
                    }
                });
            }
        };
        MqttConnectManager.getInstance().addUpdater(mUpdaterMqtt);
        HttpUtil.subscribeNetworkTask(new DrinkingModel().getCupList(), null);
    }

    public void pauseListening() {
        BleConnectManager.getInstance().removeUpdater(mUpdaterBle);
        MqttConnectManager.getInstance().removeUpdater(mUpdaterMqtt);
        BleConnectManager.getInstance().disconnectServer();
        MqttConnectManager.getInstance().disconnectServer();
        WifiConnectManager.getInstance().disconnectServer();
        mainHandler = null;
    }

    public void destroy() {
        pauseListening();
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
//            } else if (data.equals("AA CC DD 03 01 DD CC AA")) {//电解中
            } else if (data.startsWith("AA CC DD 03 01")) {//电解中
                messageProxyCallback.onElectrolyzing(uuid);
            } else if (data.equals("AA CC DD 03 02 DD CC AA")) {//电解结束
                messageProxyCallback.onElectrolyzeEnd(uuid);
//            } else if (data.startsWith("AA CC DD 03 03 DD CC AA")) {//冲洗中
            } else if (data.startsWith("AA CC DD 03 03")) {//冲洗中
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
            //P是电量
            //H是 電解 or清洗 timer
            //M是模式， 0 是idle,  1 是電解中， 2 是清洗中
            if (split[0].equals("P")) {//电量数据

            } else if (split[2].equals("H")) {

                //Update hydro timer  --> if split[4] == M and split[5] == 0
                //Update clean timer --> if split[4] == M and split[5] == 1
                //If hydro timer == 0 and split[5] == 0 --> Hydro Finish
                //If hydro timer == 0 and split[5] == 1 --> Clean Finish
            }
            if (split.length >= 6 && split[0].equals("P") && split[2].equals("H") && split[4].equals("M")) {
                //电量数据
                messageProxyCallback.onBattery(uuid, Integer.valueOf(split[1]));
                //时间
                Integer time = Integer.valueOf(split[3]);
                String m = "0" + (time / 60);
                String s = "0" + (time % 60);
                messageProxyCallback.onTime(uuid, m.substring(m.length() - 2), s.substring(s.length() - 2));
                switch (split[5]) {
                    case "0": {//idle
                        break;
                    }
                    case "1": {//電解中
                        if (time == 0) {
                            messageProxyCallback.onElectrolyzeEnd(uuid);
                        } else {
                            messageProxyCallback.onElectrolyzing(uuid);
                        }
                        break;
                    }
                    case "2": {//清洗中
                        if (time == 0) {
                            messageProxyCallback.onCleaneEnd(uuid);
                        } else {
                            messageProxyCallback.onCleaning(uuid);
                        }
                        break;
                    }
                }
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
