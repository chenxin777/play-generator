package com.chenxin.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chenxin.maker.mata.Meta;
import com.chenxin.maker.mata.enums.FileGenerateTypeEnum;
import com.chenxin.maker.mata.enums.FileTypeEnum;
import com.chenxin.maker.template.enums.FileFilterRangeEnum;
import com.chenxin.maker.template.enums.FileFilterRuleEnum;
import com.chenxin.maker.template.model.FileFilterConfig;
import com.chenxin.maker.template.model.TemplateMakerFileConfig;
import com.chenxin.maker.template.model.TemplateMakerModelConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fangchenxin
 * @description 模版制作工具
 * @date 2024/7/23 16:39
 * @modify
 */
public class TemplateMaker {

    private static long makeTemplate(Meta newMeta, String originProjectPath, TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, Long id) {
        // 没有id，则生成
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }

        // 指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        // 复制目录
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }
        // 要挖坑的项目根目录
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();

        // 处理文件信息
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String inputFilePath = fileInfoConfig.getPath();
            String inputFileAbsolutePath = sourceRootPath + File.separator + inputFilePath;
            List<File> fileList = FileFilter.doFilter(inputFileAbsolutePath, fileInfoConfig.getFilterConfigList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileConfigInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file);
                newFileInfoList.add(fileConfigInfo);
            }
        }
        // 如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            // 分组放到1个分组内
            groupFileInfo.setFiles(newFileInfoList);

            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }

        // 处理模型信息
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        // 转换为配置文件接受的modelInfo对象
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = models.stream()
                .map(modelInfoConfig -> {
                    Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
                    BeanUtil.copyProperties(modelInfoConfig, modelInfo);
                    return modelInfo;
                })
                .collect(Collectors.toList());
        // 本次新增的模型列表
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        // 如果是模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            String condition = modelGroupConfig.getCondition();
            String groupKey = modelGroupConfig.getGroupKey();
            String groupName = modelGroupConfig.getGroupName();

            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            groupModelInfo.setGroupKey(groupKey);
            groupModelInfo.setGroupName(groupName);
            groupModelInfo.setCondition(condition);

            // 模型放到1个分组
            groupModelInfo.setModels(inputModelInfoList);
            newModelInfoList.add(groupModelInfo);
        } else {
            newModelInfoList.addAll(inputModelInfoList);
        }

        // 生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        // 已有meta文件，不是第一次制作，则在mata基础上进行修改
        if (FileUtil.exist(metaOutputPath)) {
            newMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            // 追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);

            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);

            // 配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelInfoList));

        } else {
            // 构造配置参数
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            fileConfig.setSourceRootPath(sourceRootPath);

            List<Meta.FileConfig.FileInfo> fileConfigInfoList = new ArrayList<>();
            fileConfigInfoList.addAll(newFileInfoList);
            fileConfig.setFiles(fileConfigInfoList);
            newMeta.setFileConfig(fileConfig);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            List<Meta.ModelConfig.ModelInfo> modelConfigInfoList = new ArrayList<>();
            modelConfigInfoList.addAll(newModelInfoList);
            modelConfig.setModels(modelConfigInfoList);

            newMeta.setModelConfig(modelConfig);

        }
        // 输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);

        return id;
    }

    /**
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @param inputFile
     * @return com.chenxin.maker.mata.Meta.FileConfig.FileInfo
     * @description 制作文件模版
     * @author fangchenxin
     * @date 2024/7/28 18:13
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, File inputFile) {
        // 要挖坑的文件（相对路径）
        String fileInputPath = inputFile.getAbsolutePath().replace(sourceRootPath + "/", "");
        // 输出文件模版文件（相对路径）
        String fileOutputPath = fileInputPath + ".ftl";

        // 使用字符串替换，生成模版文件(绝对路径)
        String fileInputAbsolutePath = inputFile.getAbsolutePath();
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        // 如果已有模版文件，表示不是第一次制作，则在原有模版的基础上挖坑
        String fileContent;
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        // 支持多个模型：对于同一个文件的内容，遍历模型进行多轮替换
        String newFileContent = fileContent;
        String replacement;
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            String fieldName = modelInfoConfig.getFieldName();
            // 不是分组
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", fieldName);
            } else {
                String groupKey = modelGroupConfig.getGroupKey();
                replacement = String.format("${%s.%s}", groupKey, modelInfoConfig.getFieldName());
            }
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }

        // 封装文件参数
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getCode());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getCode());

        // 和原文件内容一致，没有挖坑，静态生成
        if (newFileContent.equals(fileContent)) {
            // 输入路径等于输出路径
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getCode());
        } else {
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getCode());
            // 输出模版文件
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }

    public static void main(String[] args) {
        // 项目基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模版生成器");

        // 指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        // 要挖坑的项目根目录
        String originProjectPath = new File(projectPath).getParent() + File.separator + "play-generator-demo-projects/springboot-init";
        // 文件路径
        String fileInputPath1 = "src/main/java/com/play/springbootinit/common";
        String fileInputPath2 = "src/main/resources/application.yml";

        long id = 1815990045473722368L;

        // 文件过滤
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(fileInputPath1);
        ArrayList<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NAME.getCode())
                .rule(FileFilterRuleEnum.CONTAINS.getCode())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);

        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1, fileInfoConfig2);
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(fileInfoConfigList);

        // 分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("out2");
        fileGroupConfig.setGroupKey("test3");
        fileGroupConfig.setGroupName("测试分组3");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        // 单个模型配置
        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoList = new ArrayList<>();
        TemplateMakerModelConfig.ModelInfoConfig modelInfo1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfo1.setFieldName("basePackage");
        modelInfo1.setType("String");
        modelInfo1.setDefaultValue("com.chenxin");
        modelInfoList.add(modelInfo1);

        templateMakerModelConfig.setModels(modelInfoList);

        // 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        // 模型参数配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3377/chen");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3377/chen");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, id);
    }

    /**
     * @param fileInfoList
     * @return java.util.List<com.chenxin.maker.mata.Meta.FileConfig.FileInfo>
     * @description 文件去重
     * @author fangchenxin
     * @date 2024/7/24 16:23
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 将所有文件配置(fileInfo)分为有分组和无分组的
        Map<String, List<Meta.FileConfig.FileInfo>> fileInfoListMapByGroupKey = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotEmpty(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));
        // 对有分组的文件配置，如果有相同的分组，同分组内的文件进行合并，不同分组同时保留
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : fileInfoListMapByGroupKey.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r))
                    .values());
            // 使用新的group配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            groupKeyMergedFileInfoMap.put(entry.getKey(), newFileInfo);
        }
        // 创建新的文件配置列表（结果列表），先将合并后的分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());
        //  再将无分组的文件配置列表添加到结果列表
        resultList.addAll(fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)
                ).values());
        return resultList;
    }

    /**
     * @param modelInfoList
     * @return java.util.List<com.chenxin.maker.mata.Meta.ModelConfig.ModelInfo>
     * @description 模型去重
     * @author fangchenxin
     * @date 2024/7/24 16:36
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {

        // 将所有模型配置(modelInfo)分为有分组和无分组的
        Map<String, List<Meta.ModelConfig.ModelInfo>> modelInfoListMapByGroupKey = modelInfoList.stream()
                .filter(modelInfo -> StrUtil.isNotEmpty(modelInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey));
        // 对有分组的模型配置，如果有相同的分组，同分组内的模型进行合并，不同分组同时保留
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : modelInfoListMapByGroupKey.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfig.ModelInfo> newFileInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r))
                    .values());
            // 使用新的group配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newFileInfoList);
            groupKeyMergedModelInfoMap.put(entry.getKey(), newModelInfo);
        }
        // 创建新的模型配置列表（结果列表），先将合并后的分组添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());
        //  再将无分组的模型配置列表添加到结果列表
        resultList.addAll(modelInfoList.stream()
                .filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values());
        return resultList;
    }
}
