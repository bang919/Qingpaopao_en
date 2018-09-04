package com.wopin.qingpaopao.command;

public abstract class IConnectDeviceCommand<T> implements ICommand {

    private T target;

    public IConnectDeviceCommand(T target) {
        this.target = target;
    }

    public T getTarget() {
        return target;
    }
}
