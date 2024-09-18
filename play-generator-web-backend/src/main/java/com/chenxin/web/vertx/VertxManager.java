package com.chenxin.web.vertx;

import com.chenxin.web.manager.CacheManager;
import com.qcloud.cos.transfer.TransferManager;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description
 * @author fangchenxin
 * @date 2024/9/4 19:10
 * @modify
 */
@Slf4j
@Component
public class VertxManager {

    @Resource
    private CacheManager cacheManager;

    @PostConstruct
    public void init() {
        log.info("VertxManager 初始化成功");
        Vertx vertx = Vertx.vertx();
        MainVerticle mainVerticle = new MainVerticle(cacheManager);
        vertx.deployVerticle(mainVerticle);

    }
}
