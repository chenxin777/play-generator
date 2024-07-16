package com.chenxin.cli.pattern;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 17:00
 * @modify
 */
@Data
public class RemoteControl {

    private static List<Command> historyCommands = new ArrayList<>();

    private Command command;

    public void pressButton() {
        historyCommands.add(command);
        command.execute();
    }


}
