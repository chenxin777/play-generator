package com.chenxin.maker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fangchenxin
 * @description 模版文件配置
 * @date 2024/7/25 18:23
 * @modify
 */
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    private FileGroupConfig fileGroupConfig;

    @NoArgsConstructor
    @Data
    public static class FileInfoConfig {
        /**
         * 文件路径
         */
        private String path;

        /**
         * 控制条件
         */
        private String condition;

        /**
         * 文件过滤规则
         */
        private List<FileFilterConfig> filterConfigList;
    }

    @Data
    public static class FileGroupConfig {

        private String condition;

        private String groupKey;

        private String groupName;

    }

}
