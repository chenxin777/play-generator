package com.chenxin.maker.template.model;

import lombok.Data;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/30 17:57
 * @modify
 */
@Data
public class TemplateMakerOutputConfig {

    // 从未分组的文件中移除组内的同名文件
    private boolean removeGroupFilesFromRoot = true;
}
