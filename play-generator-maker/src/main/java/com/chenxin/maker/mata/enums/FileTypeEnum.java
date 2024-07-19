package com.chenxin.maker.mata.enums;

/**
 * @author fangchenxin
 * @description 文件类型枚举
 * @date 2024/7/19 18:42
 * @modify
 */
public enum FileTypeEnum {
    DIR("目录", "dir"),
    FILE("文件", "file");

    private String text;
    private String code;

    FileTypeEnum(String text, String code) {
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
