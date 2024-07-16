package com.chenxin.cli.pattern;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/15 16:49
 * @modify
 */
public class Device {

    private String name;

    public Device(String name) {
        this.name = name;
    }

    public void ternOn() {
        System.out.println(name + " 设备打开");
    }

    public void ternOff() {
        System.out.println(name + " 设备关闭");
    }
}
