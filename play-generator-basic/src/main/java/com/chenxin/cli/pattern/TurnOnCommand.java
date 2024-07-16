package com.chenxin.cli.pattern;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 16:48
 * @modify
 */
public class TurnOnCommand implements Command {

    private Device device;

    public TurnOnCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.ternOn();
    }
}
