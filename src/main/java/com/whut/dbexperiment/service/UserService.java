package com.whut.dbexperiment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.dbexperiment.entity.User;
import com.whut.dbexperiment.entity.UserDto;

public interface UserService extends IService<User> {

    /**
     * 获取所有人员Dto对象对应的Page对象
     *
     * @param pageAns 最终的Page对象
     */
    void getUserDtos(Page pageAns);

    /**
     * 根据传回来的UserDto对象更新某个人员所属的项目
     *
     * @param userDto 封装的UserDto对象
     */
    void updateProjUserByUserDto(UserDto userDto);

    /**
     * 根据人员的id删除和这个人员相关的所有信息，
     * 包括user表、proj_user表和proj表中的所有关联属性
     *
     * @param id 传入的用户id
     */
    void removeUserInfo(Long id);
}
