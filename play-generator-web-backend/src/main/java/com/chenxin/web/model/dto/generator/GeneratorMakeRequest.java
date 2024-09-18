package com.chenxin.web.model.dto.generator;

import com.chenxin.maker.mata.Meta;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 * @author fangchenxin
 * @date 2024/8/19 16:14
 * @modify
 */
@Data
public class GeneratorMakeRequest implements Serializable {

    private static final long serialVersionUID = -3997252185980801413L;

    /**
     * 元信息
     */
    private Meta meta;

    /**
     * 模版文件压缩包路径
     * 不包含最外层目录，进行打包
     */
    private String zipFilePath;
}
