package com.chenxin.web.job;

import cn.hutool.core.util.StrUtil;
import com.chenxin.web.manager.CosManager;
import com.chenxin.web.mapper.GeneratorMapper;
import com.chenxin.web.model.entity.Generator;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description 每天执行
 * @author fangchenxin
 * @date 2024/9/10 16:07
 * @modify
 */
@Slf4j
@Component
public class ClearCosJobHandler {

    @Resource
    private CosManager cosManager;

    @Resource
    private GeneratorMapper generatorMapper;

    @XxlJob("clearCosJobHandler")
    public void clearCosJobHandler() throws Exception {
        log.info("======== clearCosJobHandler start =========");
        // 清理无用的模版制作文件
        cosManager.delDir("/generator_make_template/");
        // 清理已删除的代码生成器对应的产物包文件
        List<Generator> deletedGeneratorList = generatorMapper.listDeletedGenerator();
        List<String> distPathList = deletedGeneratorList.stream()
                .map(Generator::getDistPath)
                .filter(StrUtil::isNotBlank)
                .map(item -> item.substring(1))
                .distinct()
                .collect(Collectors.toList());
        cosManager.delObjects(distPathList);
        log.info("======== clearCosJobHandler end =========");
    }
}
