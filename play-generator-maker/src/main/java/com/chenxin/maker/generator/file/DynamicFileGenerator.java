package com.chenxin.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/14 13:04
 * @modify
 */
public class DynamicFileGenerator {

    /**
     * @description 使用相对路径生成文件
     * @author fangchenxin
     * @date 2024/8/19 15:11
     * @param relativeInputPath 模版文件相对输入路径
     * @param outputPath
     * @param model
     */
    public static void doGenerator(String relativeInputPath, String outputPath, Object model) throws IOException, TemplateException {
        // 声明配置文件，指定FreeMarker版本号
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);

        // template/java/generator/DynamicGenerator.java.ftl
        // 获取模版文件所属包和模版名称
        int lastSplitIndex = relativeInputPath.lastIndexOf("/");
        String basePackagePath = relativeInputPath.substring(0, lastSplitIndex);
        String templateName = relativeInputPath.substring(lastSplitIndex + 1);

        // 通过类加载器获取模版
        ClassTemplateLoader templateLoader = new ClassTemplateLoader(DynamicFileGenerator.class, basePackagePath);
        cfg.setTemplateLoader(templateLoader);

        // 设置模版文件使用的字符集
        cfg.setDefaultEncoding("UTF-8");
        // 数字格式化
        cfg.setNumberFormat("0.######");
        // 加载指定模版
        Template template = cfg.getTemplate(templateName);

        // 如果文件不存在，则创建
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }
        // 创建数据模型
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);
        out.close();
    }

    public static void doGeneratorByPath(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
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

        // 如果文件不存在，则创建
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }
        // 创建数据模型
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);
        out.close();
    }

}
