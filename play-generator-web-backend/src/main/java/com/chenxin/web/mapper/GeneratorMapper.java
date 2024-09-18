package com.chenxin.web.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenxin.web.model.entity.Generator;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author fangchenxin
* @description 针对表【generator(代码生成器)】的数据库操作Mapper
* @createDate 2024-08-01 21:43:06
* @Entity generator.domain.Generator
*/
public interface GeneratorMapper extends BaseMapper<Generator> {

    @Select("select id, distPath from generator where isDelete = 1")
    List<Generator> listDeletedGenerator();
}




