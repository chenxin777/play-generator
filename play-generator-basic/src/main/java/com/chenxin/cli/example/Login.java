package com.chenxin.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/14 22:34
 * @modify
 */
@CommandLine.Command(subcommands = {ASCIIArt.class})
public class Login implements Callable<Integer> {

    @Option(names = {"-u", "--user"}, description = "User name")
    String user;

    @Option(names = {"-p", "--password"}, description = "Password", interactive = true, prompt = "please input password: ", arity = "0..1")
    String password;

    @Option(names = {"-cp", "--checkPassword"}, description = "Check Password", interactive = true, prompt = "please input the same password: ", arity = "0..1")
    String checkPassword;


    @Override
    public Integer call() throws Exception {
        System.out.println("name = " + user);
        System.out.println("password = " + password);
        System.out.println("checkPassword = " + checkPassword);
        return 0;
    }

    public static void main(String[] args) {
        String[] options = new String[]{"-u", "fcx"};
        new CommandLine(new Login()).execute(addArgs(options));
    }

    public static String[] addArgs(String[] args) {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        Field[] declaredFields = Login.class.getDeclaredFields();
        List<List<String>> optionList = Arrays.stream(declaredFields).filter(field -> field.getAnnotation(Option.class).interactive()).map(field -> Arrays.asList(field.getAnnotation(Option.class).names())).collect(Collectors.toList());
        List<String> list = optionList.stream().flatMap(List::stream).collect(Collectors.toList()).stream().filter(item -> !item.startsWith("--")).collect(Collectors.toList());
        for (String option : list) {
            if (!argList.contains(option)) {
                argList.add(option);
            }
        }
        return argList.toArray(new String[0]);
    }

}
