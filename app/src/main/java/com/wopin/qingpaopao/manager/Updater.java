package com.wopin.qingpaopao.manager;

public abstract class Updater<T> {
    public void onConnectDevice(T t) {
    }

    public void onDissconnectDevice(T t) {
    }

    public void onDatasUpdate(T t) {
    }
}
