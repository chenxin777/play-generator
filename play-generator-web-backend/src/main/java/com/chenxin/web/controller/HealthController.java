package com.chenxin.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author <a href="https://github.com/chenxin777">玩物志出品</a>
 */
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @GetMapping
    public String healthCheck() {
        return  "OK";
    }

}
