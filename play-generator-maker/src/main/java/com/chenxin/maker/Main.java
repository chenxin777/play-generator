package com.chenxin.maker;

import com.chenxin.maker.generator.main.MainGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author fangchenxin
 * @description 启动入口
 * @date 2024/7/16 12:13
 * @modify
 */
public class Main {

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }
}
