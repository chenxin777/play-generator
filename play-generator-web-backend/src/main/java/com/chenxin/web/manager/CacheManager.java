package com.chenxin.web.manager;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.json.JSONUtil;
import com.chenxin.web.model.dto.generator.GeneratorQueryRequest;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存操作
 *
 * @author <a href="https://github.com/chenxin777">玩物志出品</a>
 *
 */
@Slf4j
@Component
public class CacheManager {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    Cache<String, Object> localCache = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * @description 获取分页缓存key
     * @author fangchenxin
     * @date 2024/9/2 16:47
     * @param generatorQueryRequest
     * @return java.lang.String
     */
    public String getPageCacheKey(GeneratorQueryRequest generatorQueryRequest) {
        String jsonStr = JSONUtil.toJsonStr(generatorQueryRequest);
        // 请求参数编码
        String base64 = Base64Encoder.encode(jsonStr);
        return "generator:page:" + base64;
    }

    /**
     * @description 添加缓存
     * @author fangchenxin
     * @date 2024/9/3 11:43
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        localCache.put(key, value);
        redisTemplate.opsForValue().set(key, value, 100, TimeUnit.MINUTES);
    }

    /**
     * @description 获取缓存
     * @author fangchenxin
     * @date 2024/9/3 11:43
     * @param key
     * @return java.lang.String
     */
    public Object get(String key) {
        // 先从本地缓存中获取
        Object value = localCache.getIfPresent(key);
        if (value != null) {
            return value;
        }
        // 本地缓存未命中，从 Redis 获取
        value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            // 将Redis值存入本地缓存
            localCache.put(key, value);
        }
        return value;
    }

    /**
     * @description 删除缓存
     * @author fangchenxin
     * @date 2024/9/3 11:43
     * @param key
     */
    public void delete(String key) {
        localCache.invalidate(key);
        redisTemplate.delete(key);
    }
}
