package com.chenxin.generator;

import com.chenxin.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/14 13:51
 * @modify
 */
public class  MainGenerator {

    public static void main(String[] args) throws TemplateException, IOException {
        // 创建数据模型
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("fcx");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("结果是：");
        doGenerator(mainTemplateConfig);
    }

    public static void doGenerator(Object model) throws TemplateException, IOException {

        String inputRootPath = "/Users/fangchenxin/Desktop/yupi/code/代码生成/play-generator/play-generator-demo-projects/acm-template-pro";
        String outputRootPath = "/Users/fangchenxin/Desktop/yupi/code/代码生成/play-generator";

        String inputPath;
        String outputPath;

        // MainTemplate
        inputPath = new File(inputRootPath, "src/main/java/com/chenxin/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath, "src/main/java/com/chenxin/acm/MainTemplate.java").getAbsolutePath();
        DynamicGenerator.doGenerator(inputPath, outputPath, model);

        // .gitignore
        inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
        StaticGenerator.copyFilesByRecursive(inputPath, outputPath);

        // README.md
        inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
        outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        StaticGenerator.copyFilesByRecursive(inputPath, outputPath);

    }
}
