package com.chenxin.web.vertx;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenxin.web.common.ResultUtils;
import com.chenxin.web.manager.CacheManager;
import com.chenxin.web.model.dto.generator.GeneratorQueryRequest;
import com.chenxin.web.model.vo.GeneratorVO;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;

/**
 * @description
 * @author fangchenxin
 * @date 2024/9/4 16:37
 * @modify
 */
public class MainVerticle extends AbstractVerticle {

    private CacheManager cacheManager;

    public MainVerticle(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
                .requestHandler(req -> {
                    HttpMethod method = req.method();
                    String path = req.path();
                    if (HttpMethod.POST.equals(method) && "/generator/page".equals(path)) {
                        // 设置请求体处理器
                        req.handler(buffer -> {
                            // 获取请求参数
                            String requestBody = buffer.toString();
                            GeneratorQueryRequest generatorQueryRequest = JSONUtil.toBean(requestBody, GeneratorQueryRequest.class);
                            // 优先缓存读取
                            String cacheKey = cacheManager.getPageCacheKey(generatorQueryRequest);
                            // 设置响应头
                            HttpServerResponse response = req.response();
                            response.putHeader("content-type", "application/json");
                            Object cacheValue = cacheManager.get(cacheKey);
                            if (cacheValue != null) {
                                response.end(JSONUtil.toJsonStr(ResultUtils.success((Page<GeneratorVO>) cacheValue)));
                                return;
                            }
                            response.end("{}");
                        });
                    }
                })
                .listen(8833)
                .onSuccess(httpServer -> System.out.println("HTTP server started on port " + httpServer.actualPort()));
    }

}
