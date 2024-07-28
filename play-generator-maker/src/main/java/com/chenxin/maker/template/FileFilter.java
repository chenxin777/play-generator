package com.chenxin.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.chenxin.maker.template.enums.FileFilterRangeEnum;
import com.chenxin.maker.template.enums.FileFilterRuleEnum;
import com.chenxin.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fangchenxin
 * @description 单个文件过滤
 * @date 2024/7/25 22:08
 * @modify
 */
public class FileFilter {

    /**
     * @description 对某个文件或目录进行过滤，返回文件列表
     * @author fangchenxin
     * @date 2024/7/26 10:40
     * @param filePath
     * @param fileFilterConfigList
     * @return java.util.List<java.io.File>
     */
    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigList) {
        List<File> fileList = FileUtil.loopFiles(filePath);
        return fileList.stream().filter(file -> doSingleFileFilter(fileFilterConfigList, file)).collect(Collectors.toList());
    }

    /**
     * @description 对单个文件过滤
     * @author fangchenxin
     * @date 2024/7/26 10:41
     * @param fileFilterConfigList
     * @param file
     * @return boolean
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        String fileName = file.getName();
        if ("ftl".equals(FileUtil.getSuffix(fileName))) {
            return false;
        }
        String fileContent = FileUtil.readUtf8String(file);

        // 所有过滤器校验结束后的结果
        boolean result = true;
        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValues(range);
            if (fileFilterRangeEnum == null) {
                continue;
            }

            String content = fileName;
            switch (fileFilterRangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValues(rule);
            if (fileFilterRuleEnum == null) {
                continue;
            }

            switch (fileFilterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }
            // 有一个不满足就返回
            if (!result) {
                return false;
            }
        }

        return result;

    }
}
