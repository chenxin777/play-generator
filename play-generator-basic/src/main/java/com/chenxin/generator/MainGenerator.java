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
public class MainGenerator {

    public static void main(String[] args) throws TemplateException, IOException {
        // 静态生成
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "play-generator-demo-projects"  + File.separator + "acm-template";
        String outputPath = projectPath;
        StaticGenerator.copyFilesByRecursive(inputPath, outputPath);

        // 动态生成
        String dynamicInputPath = projectPath + File.separator + "play-generator-basic" + File.separator + "src/main/resources/template/MainTemplate.java.ftl";
        String dynamicOutputPath = projectPath + File.separator + "acm-template/src/main/java/com/chenxin/acm/MainTemplate.java";
        // 创建数据模型
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("fcx");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("结果是：");
        DynamicGenerator.doGenerator(dynamicInputPath, dynamicOutputPath, mainTemplateConfig);

    }
}
