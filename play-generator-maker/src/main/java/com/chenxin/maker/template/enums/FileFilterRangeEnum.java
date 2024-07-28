package com.chenxin.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/25 18:35
 * @modify
 */
public enum FileFilterRangeEnum {
    FILE_NAME("文件名称", "fileName"),
    FILE_CONTENT("文件内容", "fileContent");


    private String text;

    private String code;

    FileFilterRangeEnum(String text, String code) {
        this.text = text;
        this.code = code;
    }

    public static FileFilterRangeEnum getEnumByValues(String code) {
        if (ObjectUtil.isEmpty(code)) {
            return null;
        }
        for (FileFilterRangeEnum anEnum : FileFilterRangeEnum.values()) {
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
