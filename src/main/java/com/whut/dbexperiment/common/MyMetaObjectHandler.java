package com.whut.dbexperiment.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据处理器
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 进行项目的插入操作时，自动填充项目的创建时间
     *
     * @param metaObject 获得的元数据
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");

        //设置项目的创建时间
        metaObject.setValue("beginTime", LocalDateTime.now());
    }

    //更新操作时暂不做任何处理
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
