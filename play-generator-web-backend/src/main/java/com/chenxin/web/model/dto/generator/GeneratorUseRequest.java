package com.chenxin.web.model.dto.generator;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description 生成器使用
 * @author fangchenxin
 * @date 2024/8/14 11:26
 * @modify
 */
@Data
public class GeneratorUseRequest implements Serializable {

    /**
     * 生成器id
     */
    private Long id;

    /**
     * 数据模型
     */
    private Map<String, Object> dataModel;

    private static final long serialVersionUID = 1L;
}
