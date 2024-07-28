package com.chenxin.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/25 18:35
 * @modify
 */
public enum FileFilterRuleEnum {
    CONTAINS("包含", "contains"),
    STARTS_WITH("前缀", "startsWith"),
    ENDS_WITH("后缀", "endsWith"),
    REGEX("正则", "regex"),
    EQUALS("等于", "equals");


    private String text;

    private String code;

    FileFilterRuleEnum(String text, String code) {
        this.text = text;
        this.code = code;
    }

    public static FileFilterRuleEnum getEnumByValues(String code) {
        if (ObjectUtil.isEmpty(code)) {
            return null;
        }
        for (FileFilterRuleEnum anEnum : FileFilterRuleEnum.values()) {
            if (anEnum.code.equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public String getCode() {
        return code;
    }
}
