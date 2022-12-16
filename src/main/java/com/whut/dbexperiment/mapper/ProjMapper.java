package com.whut.dbexperiment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whut.dbexperiment.entity.Proj;
import org.apache.ibatis.annotations.Mapper;

//MyBatisPlus中的注解，令本接口为SpringBoot容器中对数据库中直接进行操作的接口类
@Mapper
public interface ProjMapper extends BaseMapper<Proj> {
}
