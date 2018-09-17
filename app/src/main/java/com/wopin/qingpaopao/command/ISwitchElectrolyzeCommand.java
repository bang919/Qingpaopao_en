package com.wopin.qingpaopao.command;

public abstract class ISwitchElectrolyzeCommand<T> implements ICommand {

    private T target;
    private int time;

    public ISwitchElectrolyzeCommand(T target, int time) {
        this.target = target;
        this.time = time;
    }

    public T getTarget() {
        return target;
    }

    public int getTime() {
        return time;
    }
}
