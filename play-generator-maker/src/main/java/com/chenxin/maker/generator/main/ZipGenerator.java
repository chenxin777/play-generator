package com.chenxin.maker.generator.main;

/**
 * @author fangchenxin
 * @description 生成代码生成器压缩包
 * @date 2024/7/17 16:45
 * @modify
 */
public class ZipGenerator extends GenerateTemplate{

    @Override
    protected String buildDist(String outputPath, String jarPath, String shellOutputFilePath, String sourceCopyDestPath) {
        String disPath = super.buildDist(outputPath, jarPath, shellOutputFilePath, sourceCopyDestPath);
        return super.buildZip(disPath);
    }
}
