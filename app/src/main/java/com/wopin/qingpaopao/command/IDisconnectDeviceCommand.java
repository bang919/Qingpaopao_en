package com.wopin.qingpaopao.command;

public abstract class IDisconnectDeviceCommand<T> implements ICommand {

    private T target;

    public IDisconnectDeviceCommand(T target) {
        this.target = target;
    }

    public T getTarget() {
        return target;
    }
}
