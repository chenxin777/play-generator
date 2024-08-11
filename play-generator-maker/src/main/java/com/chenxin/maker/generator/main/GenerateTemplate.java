package com.chenxin.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.chenxin.maker.generator.GitGenerator;
import com.chenxin.maker.generator.JarGenerator;
import com.chenxin.maker.generator.ScriptGenerator;
import com.chenxin.maker.generator.file.DynamicFileGenerator;
import com.chenxin.maker.mata.Meta;
import com.chenxin.maker.mata.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/19 19:36
 * @modify
 */
public class GenerateTemplate {

    public static final String JAVA_PATH = "src/main/java/";

    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();
        // 输出根路径
        String projectPath = System.getProperty("user.dir");
        // 制作工具根路径
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        // 生成jar包名
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        // 生成jar包路径
        String jarPath = "target" + File.separator + jarName;
        // 生成脚本路径
        String shellOutputFilePath = outputPath + File.separator + "generator";
        // 生成工具包根路径
        if (FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        // 1、复制原始文件
        String sourceCopyDestPath = copySource(meta, outputPath);

        // 2、代码生成
        generateCode(meta, outputPath);

        // 3、构建jar包
        buildJar(outputPath);

        // 4、封装脚本
        buildScript(shellOutputFilePath, jarPath);

        // 5、生成精简版
        buildDist(outputPath, jarPath, shellOutputFilePath, sourceCopyDestPath);

        // 6、Git托管
        buildGit(meta, outputPath);
    }

    /**
     * @description 制作启动脚本
     * @author fangchenxin
     * @date 2024/8/8 15:56
     * @param shellOutputFilePath
     * @param jarPath 制作工具jar包路径
     */
    protected void buildScript(String shellOutputFilePath, String jarPath) {
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
    }

    /**
     * @description 构建git
     * @author fangchenxin
     * @date 2024/8/8 15:57
     * @param meta
     * @param outputPath
     */
    protected void buildGit(Meta meta, String outputPath) throws IOException, InterruptedException {
        if (meta.getIsGit()) {
            GitGenerator.doGenerate(outputPath);
        }
    }

    /**
     * @description 制作精简版程序包
     * @author fangchenxin
     * @date 2024/8/8 15:59
     * @param outputPath
     * @param jarPath
     * @param shellOutputFilePath
     * @param sourceCopyDestPath
     */
    protected String buildDist(String outputPath, String jarPath, String shellOutputFilePath, String sourceCopyDestPath) {
        // 生成精简版的程序（产物包）
        String distOutputPath = outputPath + "-dist";
        // - 拷贝 jar 包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        // - 拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        FileUtil.copy(shellOutputFilePath + ".bat", distOutputPath, true);
        // - 拷贝源模版文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
        return distOutputPath;
    }

    /**
     * @description 制作jar包
     * @author fangchenxin
     * @date 2024/8/8 15:59
     * @param outputPath
     */
    protected void buildJar(String outputPath) throws InterruptedException, IOException {
        JarGenerator.doGenerate(outputPath);
    }

    /**
     * @description 生成代码
     * @author fangchenxin
     * @date 2024/8/8 16:00
     * @param meta
     * @param outputPath
     */
    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
        // 读取resources目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();

        // Java包的基础路径
        // com.xxx
        String outputBasePackage = meta.getBasePackage();
        // com/xxx
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        // generated/src/main/java/com/xxx/
        String outputBaseJavaPackagePath = outputPath + File.separator + JAVA_PATH + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        // model.DataModel
        inputFilePath = inputResourcePath + File.separator + "template/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/model/DataModel.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // generator.MainGenerator
        inputFilePath = inputResourcePath + File.separator + "template/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/generator/MainGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // cli.command.GenerateCommand
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // cli.command.ConfigCommand
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // cli.command.ListCommand
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // cli.CommandExecutor
        inputFilePath = inputResourcePath + File.separator + "template/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // Main
        inputFilePath = inputResourcePath + File.separator + "template/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "Main.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // generator.DynamicGenerator
        inputFilePath = inputResourcePath + File.separator + "template/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // generator.StaticGenerator
        inputFilePath = inputResourcePath + File.separator + "template/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // pom
        inputFilePath = inputResourcePath + File.separator + "template/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        // README
        inputFilePath = inputResourcePath + File.separator + "template/README.md.ftl";
        outputFilePath = outputPath + File.separator + "README.md";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
    }

    /**
     * @description 复制源程序
     * @author fangchenxin
     * @date 2024/8/8 16:01
     * @param meta
     * @param outputPath
     * @return java.lang.String
     */
    protected String copySource(Meta meta, String outputPath) {
        // 从原始模版文件路径复制到生成的代码包中
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }

    /**
     * @description 制作压缩包
     * @author fangchenxin
     * @date 2024/8/8 15:15
     * @param outputPath 产物输出路径
     * @return java.lang.String 压缩包路径
     */
    protected String buildZip(String outputPath) {
        String zipPath = outputPath + ".zip";
        File zippedFile = ZipUtil.zip(outputPath, zipPath);
        return zipPath;
    }
}
