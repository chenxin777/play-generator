package com.chenxin.maker.mata;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author fangchenxin
 * @description
 * @date 2024/7/17 16:11
 * @modify
 */
public class MetaManager {

    private static volatile Meta meta;

    /**
     * @description 双检锁单例模式
     * @author fangchenxin
     * @date 2024/7/17 16:35
     * @return com.chenxin.maker.mata.Meta
     */
    public static Meta getMetaObject() {
        if (meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    /**
     * @description 读取元信息
     * @author fangchenxin
     * @date 2024/7/17 16:44
     * @return com.chenxin.maker.mata.Meta
     */
    private static Meta initMeta() {
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        //String metaJson = ResourceUtil.readUtf8Str("springboot-init-meta.json");
        Meta meta = JSONUtil.toBean(metaJson, Meta.class);
        MetaValidator.doValidateAndFill(meta);
        return meta;
    }

}
