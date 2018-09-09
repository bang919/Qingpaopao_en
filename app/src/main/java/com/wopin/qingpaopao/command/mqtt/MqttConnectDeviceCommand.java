package com.wopin.qingpaopao.command.mqtt;

import com.wopin.qingpaopao.command.IConnectDeviceCommand;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConnectDeviceCommand extends IConnectDeviceCommand<String> {

    private MqttClient mMqttClient;

    public MqttConnectDeviceCommand(MqttClient mqttClient, String target) {
        super(target);
        mMqttClient = mqttClient;
    }

    @Override
    public void execute() {
        subscribe(getTarget());
        mMqttClient = null;
    }

    public boolean subscribe(String topicName) {
        topicName = topicName + "-D";
        boolean flag = false;

        if (mMqttClient != null && mMqttClient.isConnected()) {
            try {
                mMqttClient.subscribe(topicName, 0);
                flag = true;
            } catch (MqttException e) {

            }
        }
        return flag;
    }
}
