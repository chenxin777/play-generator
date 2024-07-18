package com.chenxin.maker.generator.file;

import cn.hutool.core.io.FileUtil;

/**
 * @author fangchenxin
 * @description 静态文件生成器
 * @date 2024/7/13 10:25
 * @modify
 */
public class StaticFileGenerator {

    /**
     * @description 拷贝文件（Hutool实现，会将输入目录完整拷贝到输出目录下）
     * @author fangchenxin
     * @date 2024/7/13 10:29
     * @param inputPath 输入路径
     * @param outputPath 输出路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
