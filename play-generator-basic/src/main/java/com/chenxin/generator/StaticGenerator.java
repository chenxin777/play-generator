package com.chenxin.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author fangchenxin
 * @description 静态文件生成器
 * @date 2024/7/13 10:25
 * @modify
 */
public class StaticGenerator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "play-generator-demo-projects"  + File.separator + "acm-template";
        System.out.println("inputPath: " + inputPath);
        String outputPath = projectPath;
        copyFilesByRecursive(inputPath, outputPath);
    }

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

    /**
     * @description 递归拷贝文件（递归实现，将输入目录完整拷贝到输出目录）
     * @author fangchenxin
     * @date 2024/7/13 13:04
     * @param inputPath
     * @param outputPath
     */
    public static void copyFilesByRecursive(String inputPath, String outputPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        try {
            copyFileByRecursive(inputFile, outputFile);
        } catch (Exception ex) {
            System.out.println("文件复制失败");
            ex.printStackTrace();
        }
    }

    /**
     * @description
     * @author fangchenxin
     * @date 2024/7/13 12:50
     * @param inputFile
     * @param outputFile
     */
    public static void copyFileByRecursive(File inputFile, File outputFile) throws IOException {
        // 区分是文件还是目录
        if (inputFile.isDirectory()) {
            System.out.println(inputFile.getName());
            File destOutputFile = new File(outputFile, inputFile.getName());
            // 如果是目录，首先创建目标目录
            if (!destOutputFile.exists()) {
                destOutputFile.mkdirs();
            }
            // 获取目录下所有文件和子目录
            File[] files = inputFile.listFiles();
            // 无子文件，直接结束
            if (ArrayUtil.isEmpty(files)) {
                return;
            }
            for (File file : files) {
                // 递归拷贝下一层文件
                copyFileByRecursive(file, destOutputFile);
            }
        } else {
            // 是文件，直接复制到目标目录下
            Path destPath = outputFile.toPath().resolve(inputFile.getName());
            Files.copy(inputFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }


}
