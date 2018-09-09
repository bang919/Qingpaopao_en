package com.wopin.qingpaopao.manager;

import com.wopin.qingpaopao.command.mqtt.MqttColorCommand;
import com.wopin.qingpaopao.command.mqtt.MqttConnectDeviceCommand;
import com.wopin.qingpaopao.command.mqtt.MqttSwitchCleanCommand;
import com.wopin.qingpaopao.command.mqtt.MqttSwitchElectrolyzeCommand;
import com.wopin.qingpaopao.command.mqtt.MqttSwitchLightCommand;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class MqttConnectManager extends ConnectManager<MqttConnectManager.MqttUpdaterBean> {

    private static MqttConnectManager mMqttConnectManager;
    //Mqtt Related
    private static final String URL = "tcp://wifi.h2popo.com:8083";
    private static final String username = "wopin";
    private static final String password = "wopinH2popo";
    private static final String clientId = "clientId";

    private MqttClient client;
    private MqttUpdaterBean mCurrentMqttUpdaterBean;

    private MqttConnectManager() {

    }

    public static MqttConnectManager getInstance() {
        if (mMqttConnectManager == null) {
            mMqttConnectManager = new MqttConnectManager();
        }
        return mMqttConnectManager;
    }

    @Override
    protected void connectToServer(final OnServerConnectCallback onServerConnectCallback) {
        try {
            if (client == null) {
                MqttConnectOptions conOpt = new MqttConnectOptions();
                conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
                conOpt.setCleanSession(true);
                conOpt.setPassword(password.toCharArray());
                conOpt.setUserName(username);
                // Construct an MQTT blocking mode client
                String tmpDir = System.getProperty("java.io.tmpdir");
                MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
                client = new MqttClient(URL, clientId, dataStore);
                // Set this wrapper as the callback handler
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        onServerConnectCallback.onDisconnectServerCallback();
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        //收到消息
                        topic = topic.substring(0, topic.lastIndexOf("-D"));
                        MqttUpdaterBean t = new MqttUpdaterBean();
                        t.setSsid(topic);
                        t.setMessage(message.toString());
                        onDatasUpdate(t);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
                client.connect(conOpt);
            }
            onServerConnectCallback.onConnectServerCallback();
        } catch (MqttException e) {
            e.printStackTrace();
            onServerConnectCallback.onDisconnectServerCallback();
        }
    }

    @Override
    public void disconnectServer() {
        if (client != null) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            client = null;
        }
    }

    public boolean publish(String topicName, String message) {
        boolean flag = false;
        byte[] payload = message.getBytes();
        if (client != null && client.isConnected()) {
            // Create and configure a message
            MqttMessage mqttMessage = new MqttMessage(payload);
            mqttMessage.setQos(0);

            // Send the message to the server, control is not returned until
            // it has been delivered to the server meeting the specified
            // quality of service.
            try {
                client.publish(topicName, mqttMessage);
                flag = true;
            } catch (MqttException e) {

            }
        }
        return flag;
    }

    public String getCurrentSsid() {
        if (mCurrentMqttUpdaterBean != null) {
            return mCurrentMqttUpdaterBean.getSsid();
        }
        return null;
    }

    /**
     * =============================================  Device =============================================
     */

    public void connectDevice(String ssid) {
        mCurrentMqttUpdaterBean = new MqttUpdaterBean();
        mCurrentMqttUpdaterBean.setSsid(ssid);
        super.connectDevice(new MqttConnectDeviceCommand(client, ssid));
    }

    public void disconnectDevice() {
    }

    public void switchCupElectrolyze(boolean isOn) {
        if (mCurrentMqttUpdaterBean != null) {
            super.switchCupElectrolyze(new MqttSwitchElectrolyzeCommand(mCurrentMqttUpdaterBean.getSsid(), isOn));
        }
    }

    public void switchCupLight(boolean isLightOn) {
        if (mCurrentMqttUpdaterBean != null) {
            super.switchCupLight(new MqttSwitchLightCommand(mCurrentMqttUpdaterBean.getSsid(), isLightOn));
        }
    }

    public void switchCupClean(boolean isClean) {
        if (mCurrentMqttUpdaterBean != null) {
            super.switchCupClean(new MqttSwitchCleanCommand(mCurrentMqttUpdaterBean.getSsid(), isClean));
        }
    }

    public void setColor(String color) {
        if (mCurrentMqttUpdaterBean != null) {
            super.setColor(new MqttColorCommand(mCurrentMqttUpdaterBean.getSsid(), color));
        }
    }

    /**
     * =============================================  Updater =============================================
     */
    public void addUpdater(Updater<MqttUpdaterBean> updater) {
        super.addUpdater(updater);
    }

    public class MqttUpdaterBean {
        private String ssid;
        private String message;

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
