package com.chenxin.maker.generator.main;

/**
 * @author fangchenxin
 * @description 制作工具类
 * @date 2024/7/17 16:45
 * @modify
 */
public class MainGenerator extends GenerateTemplate{

    @Override
    protected String buildDist(String outputPath, String jarPath, String shellOutputFilePath, String sourceCopyDestPath) {
        System.out.println("不生成精简版");
        return "";
    }
}
