package com.chenxin.maker.mata.enums;

/**
 * @author fangchenxin
 * @description 字段类型枚举
 * @date 2024/7/19 18:42
 * @modify
 */
public enum ModelTypeEnum {
    STRING("字符串", "String"),
    BOOLEAN("布尔", "boolean");

    private String text;
    private String code;

    ModelTypeEnum(String text, String code) {
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
