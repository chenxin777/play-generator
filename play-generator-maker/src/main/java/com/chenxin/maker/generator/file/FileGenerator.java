package com.chenxin.maker.generator.file;

import com.chenxin.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/14 13:51
 * @modify
 */
public class FileGenerator {

    public static void main(String[] args) throws TemplateException, IOException {
        // 创建数据模型
        DataModel mainTemplateConfig = new DataModel();
        mainTemplateConfig.setAuthor("fcx");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("结果是：");
        doGenerator(mainTemplateConfig);
    }

    public static void doGenerator(Object model) throws TemplateException, IOException {
        // 静态生成
        String projectPath = System.getProperty("user.dir");
        // 整个项目的根路径
        File file = new File(projectPath).getParentFile();
        String inputPath =  new File(file, "play-generator-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
        // 动态生成
        String dynamicInputPath = projectPath + File.separator + "src/main/resources/template/MainTemplate.java.ftl";
        String dynamicOutputPath = outputPath + File.separator + "acm-template/src/main/java/com/chenxin/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerator(dynamicInputPath, dynamicOutputPath, model);
    }
}
