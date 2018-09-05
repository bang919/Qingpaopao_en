package com.wopin.qingpaopao.manager;

import com.wopin.qingpaopao.command.ICommand;
import com.wopin.qingpaopao.command.IConnectDeviceCommand;
import com.wopin.qingpaopao.command.IDisconnectDeviceCommand;
import com.wopin.qingpaopao.command.ISwitchCleanCommand;
import com.wopin.qingpaopao.command.ISwitchElectrolyzeCommand;

import java.util.ArrayList;

public abstract class ConnectManager<T> {

    private boolean isConnectToServer;
    private ArrayList<ICommand> mICommands = new ArrayList<>();

    private void addCommand(ICommand iCommand) {
        if (iCommand != null) {
            mICommands.add(iCommand);
        }
        //Check Connect
        if (!isConnectToServer) {
            isConnectToServer = connectToServer(new OnServerConnectCallback() {
                @Override
                public void onConnectServerCallback() {
                    execute();
                }

                @Override
                public void onDisconnectServerCallback() {
                    if (isConnectToServer) {
                        disconnectServer();
                        isConnectToServer = false;
                    }
                }
            });
        } else {
            execute();
        }
    }

    private void execute() {
        while (isConnectToServer && mICommands.size() > 0) {
            ICommand remove = mICommands.remove(0);
            remove.execute();
        }
    }

    /**
     * 连接到Ble/Wifi服务，在addComment的时候就回调用
     */
    protected abstract boolean connectToServer(OnServerConnectCallback onServerConnectCallback);

    /**
     * 断开连接，需要手动调用
     */
    public abstract void disconnectServer();

    /**
     * 连接设备
     */
    void connectDevice(IConnectDeviceCommand iConnectDeviceCommand) {
        addCommand(iConnectDeviceCommand);
    }

    /**
     * 断开连接设备
     */
    void disconnectDevice(IDisconnectDeviceCommand iDisconnectDeviceCommand) {
        addCommand(iDisconnectDeviceCommand);
    }

    /**
     * 开/关杯子电解
     */
    public void switchCupElectrolyze(ISwitchElectrolyzeCommand iSwitchElectrolyzeCommand) {
        addCommand(iSwitchElectrolyzeCommand);
    }

    /**
     * 开/关杯子清洗
     */
    public void switchCupClean(ISwitchCleanCommand iSwitchCleanCommand) {
        addCommand(iSwitchCleanCommand);
    }

    public interface OnServerConnectCallback {
        void onConnectServerCallback();

        void onDisconnectServerCallback();
    }

    /**
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  Callback  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    private ArrayList<Updater<T>> mUpdaters = new ArrayList<>();

    void addUpdater(Updater<T> updater) {
        mUpdaters.add(updater);
    }

    public void removeUpdater(Updater updater) {
        mUpdaters.remove(updater);
    }

    public void clearUpdaters() {
        mUpdaters.clear();
    }

    void onConnectDevice(T t) {
        for (Updater<T> updater : mUpdaters) {
            updater.onConnectDevice(t);
        }
    }

    void onDissconnectDevice(T t) {
        for (Updater<T> updater : mUpdaters) {
            updater.onDissconnectDevice(t);
        }
    }

    void onDatasUpdate(T t) {
        for (Updater<T> updater : mUpdaters) {
            updater.onDatasUpdate(t);
        }
    }
}
