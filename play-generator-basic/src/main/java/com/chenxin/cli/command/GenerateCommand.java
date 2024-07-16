package com.chenxin.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.chenxin.generator.MainGenerator;
import com.chenxin.model.MainTemplateConfig;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 18:15
 * @modify
 */
@Command(name = "generator.sh", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {

    /**
     * 作者
     */
    @CommandLine.Option(names = {"-a", "--author"}, description = "作者名称", interactive = true, arity = "0..1", echo = true)
    private String author = "fcx";

    /**
     * 输出信息
     */
    @CommandLine.Option(names = {"-o", "--outputText"}, description = "输出文本", interactive = true, arity = "0..1", echo = true)
    private String outputText = "输出结果：";

    /**
     * 是否循环
     */
    @CommandLine.Option(names = {"-l", "--loop"}, description = "是否循环", interactive = true, arity = "0..1", echo = true)
    private boolean loop;

    @Override
    public Integer call() {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this, mainTemplateConfig);
        try {
            MainGenerator.doGenerator(mainTemplateConfig);
        } catch (Exception e) {
            System.out.println("代码生成失败" + e);
            return -1;
        }
        return 0;
    }
}
