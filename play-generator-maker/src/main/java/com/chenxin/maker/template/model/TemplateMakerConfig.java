package com.chenxin.maker.template.model;

import cn.hutool.core.util.IdUtil;
import com.chenxin.maker.mata.Meta;
import lombok.Data;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/29 21:19
 * @modify
 */
@Data
public class TemplateMakerConfig {

    private Long id = IdUtil.getSnowflakeNextId();

    private Meta meta = new Meta();

    private String originProjectPath;

    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();

    private TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}
