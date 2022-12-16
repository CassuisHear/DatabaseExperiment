package com.whut.dbexperiment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.dbexperiment.entity.Proj;

public interface ProjService extends IService<Proj> {

    /**
     * 修改一个项目，
     * 注意Proj表和ProjUser表都需要更改
     *
     * @param proj 封装的Proj类对象
     */
    void updateOneProj(Proj proj);

    /**
     * 根据项目id删除一个项目，
     * 注意Proj表和ProjUser表都需要更改
     *
     * @param id 项目的id
     */
    void removeOneProj(Long id);
}
