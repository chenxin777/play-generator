package com.chenxin.model;

import lombok.Data;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/14 12:47
 * @modify
 */
@Data
public class MainTemplateConfig {

    //于ACM示例模版项目，可以怎么定制呢？
    //1、在代码开头增加作者@Author 注释（增加代码）
    //2、修改程序输出的信息提示（替换代码）
    //3、将循环读取输入改为单次读取（可选代码）

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
