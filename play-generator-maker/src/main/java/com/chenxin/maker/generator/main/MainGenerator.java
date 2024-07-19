package com.chenxin.maker.generator.main;

import freemarker.template.TemplateException;
import java.io.IOException;

/**
 * @author fangchenxin
 * @description 制作工具类
 * @date 2024/7/17 16:45
 * @modify
 */
public class MainGenerator extends GenerateTemplate{

    @Override
    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        super.doGenerate();
    }

    @Override
    protected void buildDist(String outputPath, String jarPath, String shellOutputFilePath, String sourceCopyDestPath) {
        System.out.println("不生成精简版");
    }
}
