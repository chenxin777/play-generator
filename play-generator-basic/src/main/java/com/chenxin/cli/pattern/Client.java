package com.chenxin.cli.pattern;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 17:04
 * @modify
 */
public class Client {

    public static void main(String[] args) {
        // 创建接收对象
        Device tv = new Device("Sony");
        Device xiaomi = new Device("Xiaomi");

        // 创建具体命令对象,绑定不同设备
        TurnOnCommand turnOnCommand = new TurnOnCommand(tv);
        TurnOffCommand turnOffCommand = new TurnOffCommand(xiaomi);

        // 创建调用者
        RemoteControl remoteControl = new RemoteControl();

        // 执行命令
        remoteControl.setCommand(turnOnCommand);
        remoteControl.pressButton();

        remoteControl.setCommand(turnOffCommand);
        remoteControl.pressButton();
    }
}
