package com.chenxin.web.model.dto.generator;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description 缓存代码生成器请求
 * @author fangchenxin
 * @date 2024/8/14 11:26
 * @modify
 */
@Data
public class GeneratorCacheRequest implements Serializable {

    /**
     * 生成器id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
