package com.chenxin.cli.example;

import picocli.CommandLine;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 14:45
 * @modify
 */
@CommandLine.Command(name = "main", mixinStandardHelpOptions = true)
public class SubCommandExample implements Runnable {
    @Override
    public void run() {
        System.out.println("执行主命令");
    }

    @CommandLine.Command(name = "add", description = "增加", mixinStandardHelpOptions = true)
    static class AddCommand implements Runnable {
        @Override
        public void run() {
            System.out.println("执行增加命令");
        }
    }

    @CommandLine.Command(name = "del", description = "删除", mixinStandardHelpOptions = true)
    static class DelCommand implements Runnable {
        @Override
        public void run() {
            System.out.println("执行删除命令");
        }
    }

    @CommandLine.Command(name = "qry", description = "查询", mixinStandardHelpOptions = true)
    static class QryCommand implements Runnable {
        @Override
        public void run() {
            System.out.println("执行查询命令");
        }
    }

    public static void main(String[] args) {
        String[] myArgs = new String[]{"ad", "add"};
//        String[] myArgs = new String[]{"--help"};
//        String[] myArgs = new String[]{"add", "--help"};
        new CommandLine(new SubCommandExample())
                .addSubcommand(new AddCommand())
                .addSubcommand(new DelCommand())
                .addSubcommand(new QryCommand())
                .execute(myArgs);
    }
}
