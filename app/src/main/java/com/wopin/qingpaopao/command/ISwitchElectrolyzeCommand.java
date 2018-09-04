package com.wopin.qingpaopao.command;

public abstract class ISwitchElectrolyzeCommand<T> implements ICommand {

    private T target;
    private boolean electrolyze;

    public ISwitchElectrolyzeCommand(T target, boolean electrolyze) {
        this.target = target;
        this.electrolyze = electrolyze;
    }

    public T getTarget() {
        return target;
    }

    public boolean isElectrolyze() {
        return electrolyze;
    }
}
