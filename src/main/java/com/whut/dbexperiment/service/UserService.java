package com.whut.dbexperiment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.dbexperiment.entity.User;

public interface UserService extends IService<User> {

    /**
     * 获取所有人员Dto对象对应的Page对象
     *
     * @param pageAns 最终的Page对象
     */
    void getUserDtos(Page pageAns);
}
