package com.chenxin.cli.pattern;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 16:46
 * @modify
 */
public class TurnOffCommand implements Command {

    private Device device;


    public TurnOffCommand(Device device) {
        this.device = device;
    }

    @Override
    public void execute() {
        device.ternOff();
    }
}
