package com.chenxin.maker.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author fangchenxin
 * @description 文件过滤配置
 * @date 2024/7/25 17:18
 * @modify
 */
@Data
@Builder
public class FileFilterConfig {

    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;


}
