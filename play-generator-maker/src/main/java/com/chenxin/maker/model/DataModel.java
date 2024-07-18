package com.chenxin.maker.model;

import lombok.Data;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/14 12:47
 * @modify
 */
@Data
public class DataModel {

    /**
     * 作者
     */
    private String author = "fcx";

    /**
     * 输出信息
     */
    private String outputText = "输出结果：";

    /**
     * 是否循环
     */
    private boolean loop;

}
