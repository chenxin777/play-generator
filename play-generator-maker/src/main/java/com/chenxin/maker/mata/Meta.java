package com.chenxin.maker.mata;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/17 16:02
 * @modify
 */
@NoArgsConstructor
@Data
public class Meta {

    private String name = "my-generator";
    private String description = "我的模版生成器";
    private String basePackage = "com.play";
    private String version = "1.0";
    private String author = "player";
    private String createTime = DateUtil.now();
    private Boolean isGit = false;
    private FileConfig fileConfig;
    private ModelConfig modelConfig;

    @NoArgsConstructor
    @Data
    public static class FileConfig {
        private String inputRootPath;
        private String outputRootPath;
        private String sourceRootPath;
        private String type;
        private List<FileInfo> files;

        @NoArgsConstructor
        @Data
        public static class FileInfo {
            private String inputPath;
            private String outputPath;
            private String type;
            private String generateType;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ModelConfig {
        private List<ModelInfo> models;

        @NoArgsConstructor
        @Data
        public static class ModelInfo {
            private String fieldName;
            private String type;
            private String description;
            private Object defaultValue;
            private String abbr;
        }
    }
}
