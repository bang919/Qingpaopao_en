package com.wopin.qingpaopao.command;

public abstract class ISwitchLightCommand<T> implements ICommand {

    private T target;
    private boolean isLightOn;

    public ISwitchLightCommand(T target, boolean isLightOn) {
        this.target = target;
        this.isLightOn = isLightOn;
    }

    public T getTarget() {
        return target;
    }

    public boolean isLightOn() {
        return isLightOn;
    }
}
