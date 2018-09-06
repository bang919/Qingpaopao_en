package com.wopin.qingpaopao.command;

public abstract class IColorCommand<T> implements ICommand {

    private T target;
    private String color;

    public IColorCommand(T target, String color) {
        this.target = target;
        this.color = color;
    }

    public T getTarget() {
        return target;
    }

    public String getColor() {
        return color;
    }
}
