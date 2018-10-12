package com.wopin.qingpaopao.manager;

import android.util.Log;

import com.wopin.qingpaopao.command.IColorCommand;
import com.wopin.qingpaopao.command.ICommand;
import com.wopin.qingpaopao.command.IConnectDeviceCommand;
import com.wopin.qingpaopao.command.IDisconnectDeviceCommand;
import com.wopin.qingpaopao.command.ISwitchCleanCommand;
import com.wopin.qingpaopao.command.ISwitchElectrolyzeCommand;
import com.wopin.qingpaopao.command.ISwitchLightCommand;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public abstract class ConnectManager<T> {

    private static final String TAG = "ConnectManager";
    private ArrayList<ICommand> mICommands = new ArrayList<>();
    private final byte[] b = new byte[0];
    private boolean hadComment;

    private void addCommand(final ICommand iCommand) {
        if (mICommands.size() >= 30) {
            return;//如果离线状态，怕累积太多command造成oom
        }
        mICommands.add(iCommand);
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "addCommand    need wait: " + (mICommands.size() != 0));
                synchronized (b) {
                    try {
                        if (hadComment) {//之前已经有comment了，等下吧
                            b.wait();
                        }
                        hadComment = true;
                        Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(final ObservableEmitter<Object> emitter) throws Exception {

                                connectToServer(new OnServerConnectCallback() {
                                    @Override
                                    public void onConnectServerCallback() {
                                        emitter.onComplete();
                                    }

                                    @Override
                                    public void onDisconnectServerCallback() {
                                        emitter.onError(new Throwable("onDisconnectServer"));
                                    }
                                });
                            }
                        })
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Object>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                    }

                                    @Override
                                    public void onNext(Object o) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        synchronized (b) {
                                            b.notify();
                                            hadComment = false;
                                        }
                                        disconnectServer();
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "complete and notify");
                                        synchronized (b) {
                                            b.notify();
                                            hadComment = false;
                                        }
                                        execute();
                                    }
                                });


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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

    public void addUpdater(Updater<T> updater) {
        mUpdaters.add(updater);
    }

    public void removeUpdater(Updater updater) {
        mUpdaters.remove(updater);
    }

    private void clearUpdaters() {
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
