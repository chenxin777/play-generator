package com.chenxin.generator;

import cn.hutool.core.date.DateUtil;
import com.chenxin.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/14 13:04
 * @modify
 */
public class DynamicGenerator {

    public static void main(String[] args) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir") + File.separator + "play-generator-basic";
        String inputPath = projectPath + File.separator + "src/main/resources/template/MainTemplate.java.ftl";
        String outputPath = projectPath + File.separator + "MainTemplate.java";
        // 创建数据模型
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("fcx");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("结果是：");
        doGenerator(inputPath, outputPath, mainTemplateConfig);
    }

    public static void doGenerator(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
        // 声明配置文件，指定FreeMarker版本号
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        File templateFile = new File(inputPath);
        // 指定模版路径
        File templatePath = templateFile.getParentFile();
        cfg.setDirectoryForTemplateLoading(templatePath);
        // 设置模版文件使用的字符集
        cfg.setDefaultEncoding("UTF-8");
        // 数字格式化
        cfg.setNumberFormat("0.######");
        // 加载指定模版
        Template template = cfg.getTemplate(templateFile.getName());
        // 创建数据模型
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);
        out.close();
    }

}
