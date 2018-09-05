package com.wopin.qingpaopao.command;

public abstract class ISwitchCleanCommand<T> implements ICommand {

    private T target;
    private boolean clean;

    public ISwitchCleanCommand(T target, boolean clean) {
        this.target = target;
        this.clean = clean;
    }

    public T getTarget() {
        return target;
    }

    public boolean isClean() {
        return clean;
    }
}
