package com.chenxin.maker.mata;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.chenxin.maker.mata.enums.FileGenerateTypeEnum;
import com.chenxin.maker.mata.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fangchenxin
 * @description 参数校验
 * @date 2024/7/19 15:50
 * @modify
 */
public class MetaValidator {

    public static void doValidateAndFill(Meta meta) {
        // fileConfig校验
        validateAndFillFileConfig(meta);
        // modelConfig校验
        validateAndFillModelConfig(meta);
    }

    private static void validateAndFillModelConfig(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<Meta.ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (CollUtil.isEmpty(models)) {
            return;
        }
        for (Meta.ModelConfig.ModelInfo modelInfo : models) {
            // 有group属性，不校验
            String groupKey = modelInfo.getGroupKey();
            if (StrUtil.isNotEmpty(groupKey)) {
                // 生成中间参数
                List<Meta.ModelConfig.ModelInfo> subModelInfoList = modelInfo.getModels();
                String allArgsStr = subModelInfoList.stream().map(subModelInfo -> String.format("\"--%s\"", subModelInfo.getFieldName())).collect(Collectors.joining(","));
                modelInfo.setAllArgsStr(allArgsStr);
                continue;
            }
            String fieldName = modelInfo.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写fieldName");
            }
            modelInfo.setType(StrUtil.blankToDefault(modelInfo.getType(), "String"));
        }
    }

    private static void validateAndFillFileConfig(Meta meta) {
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写sourceRootPath");
        }

        String sourceName = FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        String defaultInputRootPath = ".source" + File.separator + sourceName;
        fileConfig.setInputRootPath(StrUtil.blankToDefault(fileConfig.getInputRootPath(), defaultInputRootPath));

        String defaultOutputRootPath = "generated" + File.separator + sourceName;
        fileConfig.setOutputRootPath(StrUtil.blankToDefault(fileConfig.getOutputRootPath(), defaultOutputRootPath));

        String defaultFileConfigType = "dir";
        fileConfig.setType(StrUtil.blankToDefault(fileConfig.getType(), defaultFileConfigType));

        List<Meta.FileConfig.FileInfo> files = fileConfig.getFiles();
        if (CollUtil.isEmpty(files)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo : files) {
            String type = fileInfo.getType();
            if (FileTypeEnum.GROUP.getCode().equals(type)) {
                continue;
            }
            // inputPath 必填
            String inputPath = fileInfo.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写inputPath");
            }
            // outputPath 默认等于 inputPath
            fileInfo.setOutputPath(StrUtil.blankToDefault(fileInfo.getOutputPath(), inputPath));

            // type 默认 inputPath有文件后缀(.java)，为file，否则为dir
            if (StrUtil.isBlank(type)) {
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType(FileTypeEnum.DIR.getCode());
                } else {
                    fileInfo.setType(FileTypeEnum.FILE.getCode());
                }
            }
            // generateType 文件结尾不为ftl，为static，否则为dynamic
            String generateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getCode());
                } else {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getCode());
                }
            }
        }
    }
}
