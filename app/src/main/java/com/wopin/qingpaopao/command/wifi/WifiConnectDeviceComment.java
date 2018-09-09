package com.wopin.qingpaopao.command.wifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.wopin.qingpaopao.command.IConnectDeviceCommand;

import java.util.List;

public class WifiConnectDeviceComment extends IConnectDeviceCommand<WifiConfiguration> {

    private WifiManager wifiManager;

    public WifiConnectDeviceComment(WifiManager wifiManager, WifiConfiguration target) {
        super(target);
        this.wifiManager = wifiManager;
    }

    @Override
    public void execute() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration c : configuredNetworks) {
                wifiManager.disableNetwork(c.networkId);
            }
        }
        wifiManager.disconnect();
        int networkId = wifiManager.addNetwork(getTarget());
        wifiManager.enableNetwork(networkId, true);
        wifiManager.reconnect();
    }
}
