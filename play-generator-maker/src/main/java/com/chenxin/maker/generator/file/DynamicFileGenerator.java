package com.chenxin.maker.generator.file;

import cn.hutool.core.io.FileUtil;
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
