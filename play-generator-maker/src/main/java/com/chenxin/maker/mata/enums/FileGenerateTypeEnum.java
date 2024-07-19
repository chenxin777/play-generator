package com.chenxin.maker.mata.enums;

/**
 * @author fangchenxin
 * @description 文件生成类型枚举
 * @date 2024/7/19 18:42
 * @modify
 */
public enum FileGenerateTypeEnum {
    DYNAMIC("动态", "dynamic"),
    STATIC("静态", "static");

    private String text;
    private String code;

    FileGenerateTypeEnum(String text, String code) {
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public String getCode() {
        return code;
    }
}
