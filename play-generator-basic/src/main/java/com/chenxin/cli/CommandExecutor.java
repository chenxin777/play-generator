package com.chenxin.cli;

import com.chenxin.cli.command.ConfigCommand;
import com.chenxin.cli.command.GenerateCommand;
import com.chenxin.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 18:17
 * @modify
 */
@Command(name = "play", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{

    private CommandLine commandLine;

    {
        commandLine =new CommandLine(this)
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ListCommand());
    }

    @Override
    public void run() {
        System.out.println("请输入具体命令，或者输入 --help 查看命令提示");
    }

    public Integer doExecute(String[] args) {
        return commandLine.execute(args);
    }
}
