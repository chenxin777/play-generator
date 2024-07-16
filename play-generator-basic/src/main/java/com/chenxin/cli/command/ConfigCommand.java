package com.chenxin.cli.command;

import cn.hutool.core.util.ReflectUtil;
import com.chenxin.model.MainTemplateConfig;
import picocli.CommandLine;

import java.lang.reflect.Field;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 18:39
 * @modify
 */
@CommandLine.Command(name = "config", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{

    @Override
    public void run() {
        Field[] fields = ReflectUtil.getFields(MainTemplateConfig.class);
        for (Field field : fields) {
            System.out.println("字段类型：" + field.getType());
            System.out.println("字段名称" + field.getName());
        }
    }
}
