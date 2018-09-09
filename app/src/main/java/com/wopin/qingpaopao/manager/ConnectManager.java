package com.wopin.qingpaopao.manager;

import com.wopin.qingpaopao.command.IColorCommand;
import com.wopin.qingpaopao.command.ICommand;
import com.wopin.qingpaopao.command.IConnectDeviceCommand;
import com.wopin.qingpaopao.command.IDisconnectDeviceCommand;
import com.wopin.qingpaopao.command.ISwitchCleanCommand;
import com.wopin.qingpaopao.command.ISwitchElectrolyzeCommand;
import com.wopin.qingpaopao.command.ISwitchLightCommand;

import java.util.ArrayList;

public abstract class ConnectManager<T> {

    private ArrayList<ICommand> mICommands = new ArrayList<>();
    private OnServerConnectCallback mOnServerConnectCallback;

    private void addCommand(ICommand iCommand) {
        if (iCommand != null) {
            mICommands.add(iCommand);
        }
        //Check Connect
        if (mOnServerConnectCallback == null) {
            mOnServerConnectCallback = new OnServerConnectCallback() {
                @Override
                public void onConnectServerCallback() {
                    execute();
                }

                @Override
                public void onDisconnectServerCallback() {
                    disconnectServer();
                }
            };
        }
        connectToServer(mOnServerConnectCallback);
    }

    private void execute() {
        while (mICommands.size() > 0) {
            ICommand remove = mICommands.remove(0);
            remove.execute();
        }
    }

    /**
     * 连接到Ble/Wifi服务，在addComment的时候就回调用
     */
    protected abstract void connectToServer(OnServerConnectCallback onServerConnectCallback);

    /**
     * 断开连接，需要手动调用
     */
    public abstract void disconnectServer();

    public void destroy() {
        clearUpdaters();
        disconnectServer();
    }

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
    void switchCupElectrolyze(ISwitchElectrolyzeCommand iSwitchElectrolyzeCommand) {
        addCommand(iSwitchElectrolyzeCommand);
    }

    /**
     * 开/关杯子灯
     */
    public void switchCupLight(ISwitchLightCommand iSwitchLightCommand) {
        addCommand(iSwitchLightCommand);
    }

    /**
     * 开/关杯子清洗
     */
    void switchCupClean(ISwitchCleanCommand iSwitchCleanCommand) {
        addCommand(iSwitchCleanCommand);
    }

    /**
     * 改变颜色
     */
    public void setColor(IColorCommand iColorCommand) {
        addCommand(iColorCommand);
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
