package com.chenxin;

import com.chenxin.cli.CommandExecutor;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/16 12:13
 * @modify
 */
public class Main {

    public static void main(String[] args) {
        //args = new String[]{"list"};
        //args = new String[]{"generator.sh", "-l", "-a", "-o"};
        //args = new String[]{"config"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
